package pt.joaoalves03.ipvcmiddleman.modules.moodle.services

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import okhttp3.Request
import org.springframework.stereotype.Service
import pt.joaoalves03.ipvcmiddleman.HttpClient
import pt.joaoalves03.ipvcmiddleman.UnauthorizedException
import pt.joaoalves03.ipvcmiddleman.dto.AuthorizeDto
import pt.joaoalves03.ipvcmiddleman.modules.moodle.Constants
import pt.joaoalves03.ipvcmiddleman.modules.moodle.dto.AuthResponseDto
import java.io.IOException
import java.net.URLEncoder

@Service
class MoodleAuthorizationService {
  private val mapper = jacksonObjectMapper()

  fun getAuthorization(body: AuthorizeDto): String {
    val request = Request.Builder()
      .url(Constants.loginEndpoint(body.username, URLEncoder.encode(body.password, "utf-8")))
      .get()
      .build()

    try {
      HttpClient.instance.newCall(request).execute().use { response ->
        val data = response.body!!.string()

        if(data.contains("invalidlogin")) throw UnauthorizedException()

        val res = mapper.readValue<AuthResponseDto>(data)

        return res.token
      }
    } catch (e: IOException) {
      return ""
    }
  }
}