package pt.joaoalves03.ipvcmiddleman.modules.academicos.services

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.Request
import org.springframework.stereotype.Service
import pt.joaoalves03.ipvcmiddleman.HttpClient
import pt.joaoalves03.ipvcmiddleman.modules.academicos.Constants
import pt.joaoalves03.ipvcmiddleman.modules.academicos.dto.CurricularStatusDto

@Service
class CurricularUnitStatusService {
  private val mapper = jacksonObjectMapper()

  fun getCurricularUnitStatus(cookie: String): List<List<CurricularStatusDto>> {
    val request = Request.Builder()
      .url(Constants.CURRICULAR_UNIT_STATUS_ENDPOINT)
      .header("Cookie", cookie)
      .header("User-Agent", Constants.USER_AGENT)
      .get()
      .build()

    HttpClient.instance.newCall(request).execute().use { response ->
      val jsonNode = mapper.readTree(response.body!!.string())

      val values = jsonNode["result"].filter { x ->
        x["turmas"].asText().isNotEmpty()
      }

      return values.map { x ->
        CurricularStatusDto(
          evaluationDate = if (x["DT_FIM_DIS"].asText().trim() == "-") null else x["DT_FIM_DIS"].asText().trim(),
          curricularUnitId = x["CD_DISCIP"].asText(),
          schoolYear = x["CD_FMTLECT"].asText(),
          credits = x["NR_CRE_EUR_PD"].asText().split(" ")[0],
          semester = x["DS_DURACAO_PD"].asText().substringBefore("º Semestre").toInt(),
          curricularUnitName = x["DS_DISCIP"].asText(),
          finalGrade = if (x["notaFinalCalc"].asText() == "-") null else x["notaFinalCalc"].asText(),
          curricularUnitState = x["DS_STATUS"].asText(),
          evaluationType = if (x["DS_AVALIA"].asText() == "-") null else x["DS_AVALIA"].asText()
        )
      }.groupBy { it.schoolYear }.toSortedMap().values.toList()
    }
  }
}