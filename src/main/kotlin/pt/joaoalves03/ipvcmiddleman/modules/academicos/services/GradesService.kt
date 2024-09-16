package pt.joaoalves03.ipvcmiddleman.modules.academicos.services

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.Request
import pt.joaoalves03.ipvcmiddleman.HttpClient
import pt.joaoalves03.ipvcmiddleman.modules.academicos.Constants
import pt.joaoalves03.ipvcmiddleman.modules.academicos.dto.GradeDTO

object GradesService {
  private val mapper = jacksonObjectMapper()

  fun getGrades(cookie: String): List<GradeDTO> {
    val request = Request.Builder()
      .url(Constants.GRADES_ENDPOINT)
      .header("Cookie", cookie)
      .get()
      .build()

    HttpClient.instance.newCall(request).execute().use { response ->
      val jsonNode = mapper.readTree(response.body!!.string())

      return jsonNode["result"].map { x ->
        GradeDTO(
          evaluationDate = if (x["dataFimInscricao"].asText().trim() == "-") null else x["dataFimInscricao"].asText().trim(),
          curricularUnitId = x["CD_DISCIP"].asText(),
          schoolYear = x["anoLectivoCalcField"].asText(),
          credits = x["ectsCalcField"].asText().split(" ")[0],
          semester = x["CD_DURACAO"].asText(),
          curricularUnitName = x["DS_DISCIP"].asText(),
          finalGrade = if (x["notaFinalCalcField"].asText() == "-") null else x["notaFinalCalcField"].asText(),
          curricularUnitState = x["estadoCalcField"].asText(),
          evaluationType = if (x["dsAvaliaCalcField"].asText() == "-") null else x["dsAvaliaCalcField"].asText()
        )
      }
    }
  }
}