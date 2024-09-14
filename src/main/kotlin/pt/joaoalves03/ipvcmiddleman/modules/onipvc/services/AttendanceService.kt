package pt.joaoalves03.ipvcmiddleman.modules.onipvc.services

import okhttp3.FormBody
import okhttp3.Request
import org.jsoup.Jsoup
import pt.joaoalves03.ipvcmiddleman.HttpClient
import pt.joaoalves03.ipvcmiddleman.UnauthorizedException
import pt.joaoalves03.ipvcmiddleman.modules.onipvc.Constants
import pt.joaoalves03.ipvcmiddleman.modules.onipvc.dto.AttendanceCourseDTO
import pt.joaoalves03.ipvcmiddleman.modules.onipvc.dto.AttendanceYearsDTO

object AttendanceService {
  fun getAvailableYears(cookie: String): List<AttendanceYearsDTO> {
    val request = Request.Builder()
      .url(Constants.ATTENDANCE_YEARS)
      .header("Cookie", cookie)
      .get().build()

    HttpClient.instance.newCall(request).execute().use { response ->
      val data = Jsoup.parse(response.body!!.string())
      val options = data.body().select("option")

      if(options.size == 0) throw UnauthorizedException()

      return options.map { x ->
        AttendanceYearsDTO(x.attr("value"), x.text())
      }
    }
  }

  fun getAvailableCourses(cookie: String, username: String, year: String): List<AttendanceCourseDTO> {
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
        AttendanceCourseDTO(x.attr("value"), x.text())
      }
    }
  }
}