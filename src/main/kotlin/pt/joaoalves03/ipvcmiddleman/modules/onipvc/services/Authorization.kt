package pt.joaoalves03.ipvcmiddleman.modules.onipvc.services

import okhttp3.FormBody
import okhttp3.Request
import pt.joaoalves03.ipvcmiddleman.HttpClient
import pt.joaoalves03.ipvcmiddleman.dto.AuthorizeDTO
import pt.joaoalves03.ipvcmiddleman.modules.onipvc.Constants
import pt.joaoalves03.ipvcmiddleman.IncorrectCredentialsException
import java.io.IOException

object Authorization {
  fun getAuthorization(body: AuthorizeDTO): String {
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

        return response.headers.values("Set-Cookie")
          .find { it.startsWith("PHPSESSID") }
          ?.substringAfter("PHPSESSID=")
          ?.substringBefore(";").orEmpty()
      }
    } catch (e: IOException) {
      return ""
    }
  }
}