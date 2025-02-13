package pt.joaoalves03.ipvcmiddleman.modules.onipvc.services

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.json.JsonReadFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import okhttp3.FormBody
import okhttp3.Request
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import org.springframework.stereotype.Service
import pt.joaoalves03.ipvcmiddleman.HttpClient
import pt.joaoalves03.ipvcmiddleman.UnauthorizedException
import pt.joaoalves03.ipvcmiddleman.modules.onipvc.Constants
import pt.joaoalves03.ipvcmiddleman.modules.onipvc.dto.ManualScheduleInitialOptionsDTO
import pt.joaoalves03.ipvcmiddleman.modules.onipvc.dto.OptionPair
import pt.joaoalves03.ipvcmiddleman.modules.onipvc.dto.RawScheduleDto
import pt.joaoalves03.ipvcmiddleman.modules.onipvc.dto.ScheduleDto

@Service
class ScheduleService {
  private fun parseSchedulesHtmlContent(content: String): List<RawScheduleDto>? {
    val regex = Regex("events_data\\s=\\s(.+);", RegexOption.MULTILINE)
    val match = regex.find(content) ?: return null

    val data = match.groupValues[1]

    return try {
      val mapper = jacksonObjectMapper()
      mapper.enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES)
      mapper.enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES)
      mapper.enable(JsonReadFeature.ALLOW_TRAILING_COMMA.mappedFeature())
      mapper.readValue<List<RawScheduleDto>>(data)
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
    if (content.contains("N/D")) return ArrayList()

    return Jsoup.parse(content)
      .text()
      .split(";")
      .map { it.replace("• ", "") }
  }

  private fun parseSchedule(body: ResponseBody): List<ScheduleDto> {
    val parsedContent = parseSchedulesHtmlContent(body.string()) ?: return ArrayList()

    return parsedContent.map { row ->
      val (shortName, classType) = parseTitle(row.title)
      val className = row.datauc.split("-")[1].trim()

      var room = Jsoup.parse(row.datasala).text().removePrefix("• ")

      if (Regex("^\\S+ - (.*)\$").matches(room)) {
        room = room.split(" - ")[1]
      }

      ScheduleDto(
        shortName,
        className,
        classType,
        start = row.start,
        end = row.end,
        id = row.dataeventoid,
        teachers = parseTeachers(row.datadocentes),
        room,
        statusColor = row.color,
      )
    }
  }

  fun getSchedule(cookie: String, year: String, semester: String, studentId: String): List<ScheduleDto> {
    val formBodyBuilder = FormBody.Builder()

    year.let {
      formBodyBuilder.add("param_anoletivoA", it)
    }
    semester.let {
      formBodyBuilder.add("param_semestreA", it)
    }
    formBodyBuilder.add("param_meuhorario_numutilizador", studentId)
    formBodyBuilder.add("param_horarios_alunos", "horario_aluno")

    val formBody = formBodyBuilder.build()

    val request = Request.Builder()
      .url(Constants.SCHEDULE_ENDPOINT)
      .header("Cookie", cookie)
      .post(formBody)
      .build()

    HttpClient.instance.newCall(request).execute().use { response ->
      return parseSchedule(response.body!!)
    }
  }

  fun getManualScheduleInitialOptions(cookie: String): ManualScheduleInitialOptionsDTO {
    val request = Request.Builder()
      .url(Constants.MANUAL_SCHEDULE_OPTIONS_ENDPOINT)
      .header("Cookie", cookie)
      .get()
      .build()

    HttpClient.instance.newCall(request).execute().use { response ->
      val data = Jsoup.parse(response.body!!.string())

      // Easy way to check auth status
      if(data.getElementById("info_anoletivo_ativo")!!.attr("value").isEmpty()) {
        throw UnauthorizedException()
      }

      return ManualScheduleInitialOptionsDTO(
        years = data
          .getElementById("param_anoletivoH")!!
          .select("option")
          .map { option ->
            OptionPair(option.text(), option.attr("value"))
          },
        semesters = data
          .getElementById("param_semestreH")!!
          .select("option")
          .map { option ->
            OptionPair(option.text(), option.attr("value"))
          },
        schools = data
          .getElementById("param_uoH")!!
          .select("option")
          .filter { option -> option.attr("value") != "0" }
          .map { option ->
            OptionPair(option.text(), option.attr("value"))
          },
        degrees = data
          .getElementById("param_grauH")!!
          .select("option")
          .filter { option -> option.attr("value") != "0" }
          .map { option ->
            OptionPair(option.text(), option.attr("value"))
          }
      )
    }
  }

  fun getCourseList(cookie: String, year: String, degree: String, school: String): List<OptionPair<String, String>> {
    val formBodyBuilder = FormBody.Builder()

    formBodyBuilder.add("param_anoletivoH", year)
    formBodyBuilder.add("param_grauH", degree)
    formBodyBuilder.add("param_uoH", school)

    val formBody = formBodyBuilder.build()

    val request = Request.Builder()
      .url(Constants.COURSE_LIST_ENDPOINT)
      .header("Cookie", cookie)
      .post(formBody)
      .build()

    HttpClient.instance.newCall(request).execute().use { response ->
      val data = Jsoup.parse(response.body!!.string())

      // Auth check
      if(data.text().contains("N/D")) {
        throw UnauthorizedException()
      }

      return data
        .getElementById("param_cursoH")!!
        .select("option")
        .filter { option -> option.attr("value") != "0" }
        .map { option ->
          OptionPair(option.text(), option.attr("value"))
        }
    }
  }

  fun getWeekList(cookie: String, year: String, semester: String): List<OptionPair<String, String>> {
    val formBodyBuilder = FormBody.Builder()

    formBodyBuilder.add("param_anoletivoH", year)
    formBodyBuilder.add("param_semestreH", semester)

    val formBody = formBodyBuilder.build()

    val request = Request.Builder()
      .url(Constants.WEEK_LIST_ENDPOINT)
      .header("Cookie", cookie)
      .post(formBody)
      .build()

    HttpClient.instance.newCall(request).execute().use { response ->
      val data = Jsoup.parse(response.body!!.string())

      // Auth check
      if(data.text().contains("N/D")) {
        throw UnauthorizedException()
      }

      return data
        .getElementById("param_semanaH")!!
        .select("option")
        .map { option ->
          OptionPair(option.text(), option.attr("value"))
        }
    }
  }

  fun getClassList(cookie: String, year: String, semester: String, course: String): List<OptionPair<String, String>> {
    val formBodyBuilder = FormBody.Builder()

    formBodyBuilder.add("param_anoletivoH", year)
    formBodyBuilder.add("param_semestreH", semester)
    formBodyBuilder.add("param_cursoH", course)

    val formBody = formBodyBuilder.build()

    val request = Request.Builder()
      .url(Constants.CLASS_LIST_ENDPOINT)
      .header("Cookie", cookie)
      .post(formBody)
      .build()

    HttpClient.instance.newCall(request).execute().use { response ->
      val data = Jsoup.parse(response.body!!.string())

      // Auth check
      if (data.text().contains("N/D")) {
        throw UnauthorizedException()
      }

      return data
        .getElementById("param_turmaH")!!
        .select("option")
        .filter { option -> option.attr("value") != "0" }
        .map { option ->
          OptionPair(option.text(), option.attr("value"))
        }
    }
  }

  fun getManualSchedule(cookie: String, year: String, semester: String, classId: String): List<ScheduleDto> {
    val weeks = getWeekList(cookie, year, semester)

    val scheduleList = mutableListOf<ScheduleDto>()

    for (week in weeks) {
      val formBodyBuilder = FormBody.Builder()

      formBodyBuilder.add("param_anoletivoH", year)
      formBodyBuilder.add("param_semestreH", semester)
      formBodyBuilder.add("param_turmaH", classId)
      formBodyBuilder.add("param_semanaH", week.value)
      formBodyBuilder.add("emissorH", "consultageral")

      val formBody = formBodyBuilder.build()

      val request = Request.Builder()
        .url(Constants.SCHEDULE_ENDPOINT)
        .header("Cookie", cookie)
        .post(formBody)
        .build()

      HttpClient.instance.newCall(request).execute().use { response ->
        scheduleList.addAll(parseSchedule(response.body!!))
      }
    }

    return scheduleList
  }
}