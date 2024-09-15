package pt.joaoalves03.ipvcmiddleman.modules.onipvc.services

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.json.JsonReadFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import okhttp3.FormBody
import okhttp3.Request
import org.jsoup.Jsoup
import pt.joaoalves03.ipvcmiddleman.HttpClient
import pt.joaoalves03.ipvcmiddleman.modules.onipvc.Constants
import pt.joaoalves03.ipvcmiddleman.modules.onipvc.dto.RawScheduleDTO
import pt.joaoalves03.ipvcmiddleman.modules.onipvc.dto.ScheduleDTO

object ScheduleService {
  private fun parseSchedulesHtmlContent(content: String): List<RawScheduleDTO>? {
    val regex = Regex("events_data\\s=\\s(.+);", RegexOption.MULTILINE)
    val match = regex.find(content) ?: return null

    val data = match.groupValues[1]

    return try {
      val mapper = jacksonObjectMapper()
      mapper.enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES)
      mapper.enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES)
      mapper.enable(JsonReadFeature.ALLOW_TRAILING_COMMA.mappedFeature())
      mapper.readValue<List<RawScheduleDTO>>(data)
    } catch (e: Exception) {
      null
    }
  }

  private fun parseTitle(content: String): Pair<String, String> {
    val regex = Regex("(.+?)\\s*\\[(.+?)]")
    val matchResult = regex.find(content)

    return matchResult?.let {
      val (beforeBracket, insideBracket) = it.destructured
      Pair(beforeBracket.trim(), insideBracket.trim())
    } ?: Pair("", "")
  }

  private fun parseTeachers(content: String): List<String> {
    if(content.contains("N/D")) return ArrayList()

    return Jsoup.parse(content)
      .text()
      .split(";")
      .map { it.replace("• ", "") }
  }

  fun getSchedule(cookie: String, year: String, semester: String, studentId: String): List<ScheduleDTO> {
    val formBody = FormBody.Builder()
      .add("param_anoletivoA", year)
      .add("param_semestreA", semester)
      .add("param_meuhorario_numutilizador", studentId)
      .add("param_horarios_alunos", "horario_aluno")
      .build()

    val request = Request.Builder()
      .url(Constants.SCHEDULE_ENDPOINT)
      .header("Cookie", cookie)
      .post(formBody)
      .build()

    HttpClient.instance.newCall(request).execute().use { response ->
      val parsedContent = parseSchedulesHtmlContent(response.body!!.string()) ?: return ArrayList()

      return parsedContent.map { row ->
        val (className, classType) = parseTitle(row.title)

        ScheduleDTO(
          className,
          classType,
          start = row.start,
          end = row.end,
          stamp = row.datauc,
          id = row.dataeventoid,
          teachers = parseTeachers(row.datadocentes),
          room = Jsoup.parse(row.datasala).text().removePrefix("• "),
          statusColor = row.color,
        )
      }
    }
  }
}