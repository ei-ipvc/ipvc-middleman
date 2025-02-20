package pt.joaoalves03.ipvcmiddleman.modules.onipvc.services

import okhttp3.FormBody
import okhttp3.Request
import org.springframework.stereotype.Service
import pt.joaoalves03.ipvcmiddleman.HttpClient
import pt.joaoalves03.ipvcmiddleman.dto.AuthorizeDto
import pt.joaoalves03.ipvcmiddleman.modules.onipvc.Constants
import pt.joaoalves03.ipvcmiddleman.IncorrectCredentialsException
import java.io.IOException

@Service
class OnIPVCAuthorizationService {
  fun getAuthorization(body: AuthorizeDto): String {
    val formBody = FormBody.Builder()
      .add("on-user", body.username)
      .add("on-pass", body.password)
      .add("on-auth", "3")
      .build()

    val request = Request.Builder()
      .url(Constants.LOGIN_ENDPOINT)
      .post(formBody)
      .build()

    try {
        HttpClient.instance.newCall(request).execute().use { response ->
        val resBody = response.body!!.string()

        if (resBody.contains("ERROR")) {
          throw IncorrectCredentialsException()
        }

        return buildString {
          append(response.headers.values("Set-Cookie")
            .find { it.startsWith("PHPSESSID") }
            ?.substringBefore(";").orEmpty())
          append(";")
          append(response.headers.values("Set-Cookie")
            .find { it.startsWith("ONIPVC=") }
            ?.substringBefore(";").orEmpty())
        }
      }
    } catch (e: IOException) {
      return ""
    }
  }
}