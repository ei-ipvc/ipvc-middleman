package pt.joaoalves03.ipvcmiddleman.modules.academicos.services

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.Request
import org.springframework.stereotype.Service
import pt.joaoalves03.ipvcmiddleman.HttpClient
import pt.joaoalves03.ipvcmiddleman.modules.academicos.Constants
import pt.joaoalves03.ipvcmiddleman.modules.academicos.dto.CurricularUnitStatusDto
import pt.joaoalves03.ipvcmiddleman.modules.academicos.dto.EvaluationDTO
import pt.joaoalves03.ipvcmiddleman.modules.academicos.dto.GradeDto
import pt.joaoalves03.ipvcmiddleman.modules.academicos.dto.evaluationType

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

      val grades = jsonNode["result"]
        .filter { x ->  x["turmasCalcField"] != null && x["estadoCalcField"].asText() != "Reprovado"}
        .mapNotNull { x ->
          GradeDto(
            evaluationDate = x["dataFimInscricao"].asText().trim(),
            curricularUnitId = x["CD_DISCIP"].asText(),
            curricularYear = x["CD_A_S_CUR"].asInt(),
            schoolYear = x["anoLectivoCalcField"].asText(),
            credits = x["ectsCalcField"].asText().split(" ")[0].toInt(),
            semester = x["CD_DURACAO"].asText().removePrefix("S").toInt(),
            curricularUnitName = x["DS_DISCIP"].asText(),
            finalGrade = if (x["dsAvaliaCalcField"].asText() == "-") null
                else x["notaFinalCalcField"].asText().toFloat(),
            curricularUnitState = x["estadoCalcField"].asText(),
            evaluationType = evaluationType.getOrDefault(x["dsAvaliaCalcField"].asText(), "UNKNOWN"),
            otherEvaluations = jsonNode["result"]
              .filter { y -> x["CD_DISCIP"] == y["CD_DISCIP"] && x["dsAvaliaCalcField"] != y["dsAvaliaCalcField"] }
              .mapNotNull { y ->
                EvaluationDTO(
                  date = y["dataFimInscricao"].asText().trim(),
                  grade = y["notaFinalCalcField"].asText().toFloat(),
                  type = evaluationType.getOrDefault(y["dsAvaliaCalcField"].asText(), "UNKNOWN")
                )
              }
          )
        }.sortedBy { x -> x.curricularYear }

      var sum = 0.0f
      var totalCredits = 0

      for (x in grades) {
        if(x.finalGrade == null) continue

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