package pt.joaoalves03.ipvcmiddleman.modules.moodle.services

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import okhttp3.Request
import org.springframework.stereotype.Service
import pt.joaoalves03.ipvcmiddleman.HttpClient
import pt.joaoalves03.ipvcmiddleman.UnauthorizedException
import pt.joaoalves03.ipvcmiddleman.dto.AuthorizeDTO
import pt.joaoalves03.ipvcmiddleman.modules.moodle.Constants
import pt.joaoalves03.ipvcmiddleman.modules.moodle.dto.AuthResponse
import java.io.IOException
import java.net.URLEncoder

@Service
class AuthorizationService {
  private val mapper = jacksonObjectMapper()

  fun getAuthorization(body: AuthorizeDTO): String {
    val request = Request.Builder()
      .url(Constants.loginEndpoint(body.username, URLEncoder.encode(body.password, "utf-8")))
      .get()
      .build()

    try {
      HttpClient.instance.newCall(request).execute().use { response ->
        val data = response.body!!.string()

        println(data)

        if(data.contains("invalidlogin")) throw UnauthorizedException()

        val res = mapper.readValue<AuthResponse>(data)

        return res.token
      }
    } catch (e: IOException) {
      return ""
    }
  }
}