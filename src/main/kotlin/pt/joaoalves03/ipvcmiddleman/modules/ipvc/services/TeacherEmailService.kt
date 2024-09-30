package pt.joaoalves03.ipvcmiddleman.modules.ipvc.services

import okhttp3.Request
import org.jsoup.Jsoup
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import pt.joaoalves03.ipvcmiddleman.HttpClient
import pt.joaoalves03.ipvcmiddleman.ServiceUnavailableException
import pt.joaoalves03.ipvcmiddleman.modules.ipvc.Constants
import pt.joaoalves03.ipvcmiddleman.modules.ipvc.dto.TeacherInfoDto
import java.text.Normalizer

@Service
class TeacherEmailService(private val redisTemplate: RedisTemplate<String, Any>) {
  private fun normalize(str: String): String {
    return Normalizer.normalize(str, Normalizer.Form.NFD)
      .replace(Regex("/[\\u0300-\\u036f]/g"), "")
  }

  private fun filterTeachersByName(
    teachers: List<TeacherInfoDto>,
    name: String,
    limit: Int = 10
  ): List<TeacherInfoDto> {
    val nameTokens = normalize(name).lowercase().split(" ")

    return teachers.filter { teacher ->
      nameTokens.all { nameInputToken ->
        normalize(teacher.name).lowercase().contains(nameInputToken)
      }
    }.take(limit)
  }

  fun fetchTeacherInfo(): List<TeacherInfoDto> {
    val request = Request.Builder()
      .url(Constants.TEACHER_INFO_ENDPOINT)
      .get()
      .build()

    HttpClient.instance.newCall(request).execute().use { response ->
      if (!response.isSuccessful) throw ServiceUnavailableException()

      val data = Jsoup.parse(response.body!!.string()).select(".corpo-docente > .link-005")

      val list = data.map { element ->
        TeacherInfoDto(
          name = element.select(".link-005-text > .link-005-item-title").text(),
          email = element.select(".link-005-email > object > a").attr("href")
            .replace("mailto:", "")
        )
      }

      saveTeacherInfo(list)

      return list
    }
  }

  fun saveTeacherInfo(teacherInfoList: List<TeacherInfoDto>) {
    redisTemplate.opsForValue().set("teacherInfo", teacherInfoList)
  }

  fun getTeacherInfoListByName(name: String): List<TeacherInfoDto> {
    @Suppress("UNCHECKED_CAST")
    val data = redisTemplate.opsForValue().get("teacherInfo") as List<TeacherInfoDto>?

    return filterTeachersByName(data ?: emptyList(), name)
  }
}