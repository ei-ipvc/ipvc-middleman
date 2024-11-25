package pt.joaoalves03.ipvcmiddleman.modules.sasocial.services

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.springframework.stereotype.Service
import pt.joaoalves03.ipvcmiddleman.HttpClient
import pt.joaoalves03.ipvcmiddleman.IncorrectCredentialsException
import pt.joaoalves03.ipvcmiddleman.dto.AuthorizeDto
import pt.joaoalves03.ipvcmiddleman.modules.sasocial.Constants
import pt.joaoalves03.ipvcmiddleman.modules.sasocial.dto.SASAuthResponse
import java.io.IOException

@Service
class SASAuthorizationService {
  private val mapper: ObjectMapper = jacksonObjectMapper()

  fun getAuthorization(body: AuthorizeDto?, refreshToken: String?): SASAuthResponse {
    val reqBody: RequestBody =
      if (body == null) "{}".toRequestBody("application/json".toMediaTypeOrNull())
      else "{\"email\": \"${body.username}@ipvc.pt\",\"password\": \"${body.password}\"}"
        .toRequestBody("application/json".toMediaTypeOrNull())

    val request = Request.Builder()
      .url(Constants.AUTHORIZATION_ENDPOINT)
      .header("Cookie", refreshToken ?: "")
      .post(reqBody)
      .build()

    try {
      HttpClient.instance.newCall(request).execute().use { response ->
        if (response.code == 401) throw IncorrectCredentialsException()

        val data = response.body!!.string()

        return SASAuthResponse(
          response.headers.values("Set-Cookie")[0]
            .removePrefix("refreshTokenWEB=").substringBefore(";"),
          mapper.readTree(data)["data"][0]["token"].asText()
        )
      }
    } catch (e: IOException) {
      return SASAuthResponse("", "")
    }
  }

  fun logout(refreshToken: String) {
    val request = Request.Builder()
      .url(Constants.LOGOUT_ENDPOINT)
      .header("Cookie", refreshToken)
      .get()
      .build()

    HttpClient.instance.newCall(request).execute()
  }
}