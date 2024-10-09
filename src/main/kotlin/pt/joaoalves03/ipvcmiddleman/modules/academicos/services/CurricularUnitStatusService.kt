package pt.joaoalves03.ipvcmiddleman.modules.academicos.services

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.Request
import org.springframework.stereotype.Service
import pt.joaoalves03.ipvcmiddleman.HttpClient
import pt.joaoalves03.ipvcmiddleman.modules.academicos.Constants
import pt.joaoalves03.ipvcmiddleman.modules.academicos.dto.CurricularUnitStatusDto
import pt.joaoalves03.ipvcmiddleman.modules.academicos.dto.GradeDto

@Service
class CurricularUnitStatusService {
  private val mapper = jacksonObjectMapper()

  fun getStatus(cookie: String): CurricularUnitStatusDto {
    val request = Request.Builder()
      .url(Constants.GRADES_ENDPOINT)
      .header("Cookie", cookie)
      .header("User-Agent", Constants.USER_AGENT)
      .get()
      .build()

    HttpClient.instance.newCall(request).execute().use { response ->
      val jsonNode = mapper.readTree(response.body!!.string())

      val grades = jsonNode["result"].mapNotNull { x ->
        if (x["dsAvaliaCalcField"].asText() == "-") null
        else GradeDto(
          evaluationDate = x["dataFimInscricao"].asText().trim(),
          curricularUnitId = x["CD_DISCIP"].asText(),
          curricularYear = x["CD_A_S_CUR"].asInt(),
          schoolYear = x["anoLectivoCalcField"].asText(),
          credits = x["ectsCalcField"].asText().split(" ")[0].toInt(),
          semester = x["CD_DURACAO"].asText().removePrefix("S").toInt(),
          curricularUnitName = x["DS_DISCIP"].asText(),
          finalGrade = x["notaFinalCalcField"].asText().toFloat(),
          curricularUnitState = x["estadoCalcField"].asText(),
          evaluationType = x["dsAvaliaCalcField"].asText()
        )
      }.sortedBy { x -> x.evaluationDate }

      var sum = 0.0f
      var totalCredits = 0

      for (x in grades) {
        // If student didn't pass this or did grade improvement exam, skip
        if ((grades.find { y ->
            y.curricularUnitName == x.curricularUnitName
                && y.evaluationType.startsWith("Melhoria")
          } != null && !x.evaluationType.startsWith("Melhoria"))
          || x.curricularUnitState == "Reprovado") continue

        sum += x.finalGrade * x.credits
        totalCredits += x.credits
      }

      return CurricularUnitStatusDto(
        average = sum / totalCredits,
        grades
      )
    }
  }


}