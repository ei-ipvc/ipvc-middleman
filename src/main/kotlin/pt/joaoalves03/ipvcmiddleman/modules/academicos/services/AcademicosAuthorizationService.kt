package pt.joaoalves03.ipvcmiddleman.modules.academicos.services

import okhttp3.FormBody
import okhttp3.Request
import org.springframework.stereotype.Service
import pt.joaoalves03.ipvcmiddleman.HttpClient
import pt.joaoalves03.ipvcmiddleman.IncorrectCredentialsException
import pt.joaoalves03.ipvcmiddleman.dto.AuthorizeDto
import pt.joaoalves03.ipvcmiddleman.modules.academicos.Constants
import java.io.IOException

@Service
class AcademicosAuthorizationService {
  fun getAuthorization(body: AuthorizeDto): String {
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

        val token = response.headers.values("Set-Cookie")
          .find { it.startsWith("JSESSIONID") }
          ?.substringBefore(";").orEmpty()

        // Before the session token is usable, a request needs to be made
        // to any page that returns a view, so the session gets "activated"
        val activationRequest = Request.Builder()
          .url(Constants.DUMMY_ACTIVATION_ENDPOINT)
          .header("Cookie", token)
          .get()
          .build()

        HttpClient.instance.newCall(activationRequest).execute().close()

        return token
      }
    } catch (e: IOException) {
      return ""
    }
  }
}