package pt.joaoalves03.ipvcmiddleman.modules.academicos.services

import okhttp3.FormBody
import okhttp3.Request
import org.springframework.stereotype.Service
import pt.joaoalves03.ipvcmiddleman.HttpClient
import pt.joaoalves03.ipvcmiddleman.IncorrectCredentialsException
import pt.joaoalves03.ipvcmiddleman.dto.AuthorizeDTO
import pt.joaoalves03.ipvcmiddleman.modules.academicos.Constants
import java.io.IOException

@Service
class AcademicosAuthorizationService {
  fun getAuthorization(body: AuthorizeDTO): String {
    val formBody = FormBody.Builder()
      .add("_formsubmitstage", "loginstage")
      .add("_formsubmitname", "login")
      .add("ajax_mode", "true")
      .add("_user", body.username)
      .add("_pass", body.password)
      .build()

    val request = Request.Builder()
      .url(Constants.LOGIN_ENDPOINT)
      .post(formBody)
      .build()

    try {
      HttpClient.instance.newCall(request).execute().use { response ->
        val resBody = response.body!!.string()

        if(resBody.contains("\"success\":false"))
          throw IncorrectCredentialsException()

        return response.headers.values("Set-Cookie")
          .find { it.startsWith("JSESSIONID") }
          ?.substringBefore(";").orEmpty()
      }
    } catch (e: IOException) {
      return ""
    }
  }
}