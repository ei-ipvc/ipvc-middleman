package pt.joaoalves03.ipvcmiddleman.modules.onipvc.services

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.FormBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.jsoup.Jsoup
import org.springframework.stereotype.Service
import pt.joaoalves03.ipvcmiddleman.HttpClient
import pt.joaoalves03.ipvcmiddleman.UnauthorizedException
import pt.joaoalves03.ipvcmiddleman.modules.onipvc.Constants
import pt.joaoalves03.ipvcmiddleman.modules.onipvc.dto.AttendanceCourseDto
import pt.joaoalves03.ipvcmiddleman.modules.onipvc.dto.AttendanceDto
import pt.joaoalves03.ipvcmiddleman.modules.onipvc.dto.AttendanceYearsDto

@Service
class AttendanceService {
  private val mapper: ObjectMapper = jacksonObjectMapper()

  fun getAvailableYears(cookie: String): List<AttendanceYearsDto> {
    val request = Request.Builder()
      .url(Constants.ATTENDANCE_YEARS)
      .header("Cookie", cookie)
      .get().build()

    HttpClient.instance.newCall(request).execute().use { response ->
      val data = Jsoup.parse(response.body!!.string())
      val options = data.body().select("option")

      if(options.size == 0) throw UnauthorizedException()

      return options.map { x ->
        AttendanceYearsDto(x.attr("value"), x.text())
      }
    }
  }

  fun getAvailableCourses(cookie: String, username: String, year: String): List<AttendanceCourseDto> {
    val formBody = FormBody.Builder()
      .add("param_alunosinscricao_idutilizador", username)
      .add("param_alunosinscricao_anoletivo", year)
      .add("param_alunosinscricao_semestre", "%")
      .build()

    val request = Request.Builder()
      .url(Constants.ATTENDANCE_COURSES)
      .header("Cookie", cookie)
      .post(formBody).build()

    HttpClient.instance.newCall(request).execute().use { response ->
      val data = Jsoup.parse(response.body!!.string())
      val options = data.body().select("option")

      if(options.size == 0) throw UnauthorizedException()

      return options.map { x ->
        AttendanceCourseDto(x.attr("value"), x.text())
      }
    }
  }

  fun getAttendance(cookie: String, courseId: String, year: String, unitId: String?): List<AttendanceDto> {
    val body: RequestBody = "{}".toRequestBody("application/json".toMediaTypeOrNull())

    val request = Request.Builder()
      .url(Constants.attendanceEndpoint(courseId, year))
      .header("Cookie", cookie)
      .post(body).build()

    HttpClient.instance.newCall(request).execute().use { response ->
      val data = response.body!!.string()

      if(data.contains("N/D"))
        throw UnauthorizedException()

      val jsonNode = mapper.readTree(data)

      val list = jsonNode["aaData"].map { row ->
        val cleanRow = row.map { cell ->
          val soup = Jsoup.parse(cell.toString())
          val div = soup.body().select("div")

          div.text()
        }

        AttendanceDto(
          cleanRow[3], cleanRow[4], cleanRow[8],
          cleanRow[9], cleanRow[10], cleanRow[11],
          cleanRow[12]
        )
      }

      if(unitId != null) {
        return list.filter { it.subjectId == unitId }
      } else {
        return list
      }
    }
  }
}