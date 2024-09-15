package pt.joaoalves03.ipvcmiddleman.modules.academicos.services

import okhttp3.Request
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import pt.joaoalves03.ipvcmiddleman.HttpClient
import pt.joaoalves03.ipvcmiddleman.IncorrectCredentialsException
import pt.joaoalves03.ipvcmiddleman.modules.academicos.Constants
import pt.joaoalves03.ipvcmiddleman.modules.academicos.dto.ProfileDTO
import java.util.*

object ProfileService {
  private fun parseCourse(profileData: Element): Pair<String, String> {
    val data = profileData.select("ul:nth-child(1) > li:nth-child(4)")
      .text().replace("[", "").replace("]", "")
      .split(" ", limit = 2)

    return Pair(data[0], data[1])
  }

  private fun getImage(data: Document, cookie: String): String {
    val img = data.select(".perfilAreaBoxPhoto > img:nth-child(1)").attr("src")

    val request = Request.Builder()
      .url(Constants.BASE_ENDPOINT + img)
      .header("Cookie", cookie)
      .header("User-Agent", Constants.USER_AGENT)
      .build()

      HttpClient.instance.newCall(request).execute().use { response ->
        if(!response.isSuccessful) {
          return ""
        }

        val imageBytes = response.body?.byteStream()?.readBytes()
        return imageBytes?.let {
          Base64.getEncoder().encodeToString(it)
        } ?: ""
      }
  }

  fun getProfile(cookie: String): ProfileDTO {
    val request = Request.Builder()
      .url(Constants.PROFILE_ENDPOINT)
      .header("Cookie", cookie)
      .header("User-Agent", Constants.USER_AGENT)
      .get()
      .build()

    HttpClient.instance.newCall(request).execute().use { response ->
      val data = Jsoup.parse(response.body!!.string())
      val profileData = data.selectFirst(".perfilAreaContent")!!

      val (courseId, courseName) = parseCourse(profileData)

      return ProfileDTO(
        profileData.select("ul:nth-child(1) > li:nth-child(3)").text(),
        profileData.select("ul:nth-child(1) > li:nth-child(2)").text().replace("Aluno Nº", ""),
        profileData.select("ul:nth-child(1) > li:nth-child(1)").text(),
        courseId,
        courseName,
        getImage(data, cookie)
      )
    }
  }
}