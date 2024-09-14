package pt.joaoalves03.ipvcmiddleman.modules.onipvc.services

import okhttp3.Request
import org.jsoup.Jsoup
import pt.joaoalves03.ipvcmiddleman.HttpClient
import pt.joaoalves03.ipvcmiddleman.UnauthorizedException
import pt.joaoalves03.ipvcmiddleman.modules.onipvc.Constants
import pt.joaoalves03.ipvcmiddleman.modules.onipvc.dto.AttendanceYearsDTO

object AttendanceService {
  fun getAvailableYears(cookie: String): List<AttendanceYearsDTO> {
    val request = Request.Builder()
      .url(Constants.ATTENDANCE_YEARS)
      .header("Cookie", cookie)
      .get().build()

    HttpClient.instance.newCall(request).execute().use { response ->
      val body = response.body!!.string()

      val data = Jsoup.parse(body)
      val options = data.body().select("option")

      if(options.size == 0) throw UnauthorizedException()

      println(body)

      return options.map { x ->
        AttendanceYearsDTO(x.attr("value"), x.text())
      }
    }
  }
}