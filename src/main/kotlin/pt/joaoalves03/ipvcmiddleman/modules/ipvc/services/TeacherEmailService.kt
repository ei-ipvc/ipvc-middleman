package pt.joaoalves03.ipvcmiddleman.modules.ipvc.services

import okhttp3.Request
import org.jsoup.Jsoup
import pt.joaoalves03.ipvcmiddleman.HttpClient
import pt.joaoalves03.ipvcmiddleman.ServiceUnavailableException
import pt.joaoalves03.ipvcmiddleman.modules.ipvc.Constants
import pt.joaoalves03.ipvcmiddleman.modules.ipvc.dto.TeacherInfoDTO
import java.text.Normalizer

object TeacherEmailService {
  private fun normalize(str: String): String {
    return Normalizer.normalize(str, Normalizer.Form.NFD)
      .replace(Regex("/[\\u0300-\\u036f]/g"), "")
  }

  fun getTeacherEmail(name: String): List<TeacherInfoDTO> {
    val request = Request.Builder()
      .url(Constants.TEACHER_INFO_ENDPOINT)
      .get()
      .build()

    HttpClient.instance.newCall(request).execute().use { response ->
      if(!response.isSuccessful) throw ServiceUnavailableException()

      val data = Jsoup.parse(response.body!!.string()).select(".corpo-docente > .link-005")

      val nameTokens = normalize(name).lowercase().split(" ")

      return data.map { element ->
        TeacherInfoDTO(
          name = element.select(".link-005-text > .link-005-item-title").text(),
          email = element.select(".link-005-email > object > a").attr("href")
            .replace("mailto:", "")
        )
      }.filter { teacher ->
        nameTokens.all { nameInputToken ->
          normalize(teacher.name).lowercase().contains(nameInputToken)
        }
      }.take(10)
    }

  }
}