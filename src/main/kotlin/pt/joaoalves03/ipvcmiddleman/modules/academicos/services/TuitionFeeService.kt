package pt.joaoalves03.ipvcmiddleman.modules.academicos.services

import okhttp3.Request
import org.jsoup.Jsoup
import org.springframework.stereotype.Service
import pt.joaoalves03.ipvcmiddleman.HttpClient
import pt.joaoalves03.ipvcmiddleman.modules.academicos.Constants
import pt.joaoalves03.ipvcmiddleman.modules.academicos.dto.TuitionFee
import java.math.BigDecimal

@Service
class TuitionFeeService {
  private fun parseValue(data: String): BigDecimal {
    return data.split(" ")[0].toBigDecimal()
  }

  fun getTuitionFees(cookie: String): List<TuitionFee> {
    val request = Request.Builder()
      .url(Constants.TUITION_FEES_ENDPOINT)
      .header("Cookie", cookie)
      .header("User-Agent", Constants.USER_AGENT)
      .get().build()

    HttpClient.instance.newCall(request).execute().use { response ->
      val data = Jsoup.parse(response.body!!.string())
      val rows = data.body().id("simpletable").select("tr")

      val tuitionFees = ArrayList<TuitionFee>()

      rows.forEach() { row ->
        val cells = row.select("td")

        if(cells.size == 9 && cells[1].text().contains("Propinas")) {
          val paymentDate = cells[5].text().trim()

          tuitionFees.add(TuitionFee(
            name = cells[1].text().trim(),
            expiryDate = cells[2].text().trim().replace("(1)", ""),
            bankReference = cells[3].text().trim(),
            value = parseValue(cells[4].text()),
            paymentDate = if (paymentDate == "N/A") "" else paymentDate,
            paid = parseValue(cells[6].text()),
            owed = parseValue(cells[7].text()),
            fine = parseValue(cells[8].text())
          ))
        }
      }

      return tuitionFees
    }
  }
}