package pt.joaoalves03.ipvcmiddleman.modules.academicos.dto

val evaluationType: Map<String, String> = mapOf(
  "Avaliação por Exame Normal" to "NORMAL",
  "Avaliação por Exame Recurso" to "RETAKE",
  "Avaliação por Exame Especial / Finalista" to "SPECIAL",
  "Melhoria(Normal)" to "IMPROVEMENT",
)

data class EvaluationDTO(
  val date: String,
  val grade: Float,
  val type: String
)

data class GradeDto(
  val evaluationDate: String,
  val curricularUnitId: String,
  val curricularYear: Int,
  val schoolYear: String,
  val credits: Int,
  val semester: Int,
  val curricularUnitName: String,
  val finalGrade: Float?,
  val curricularUnitState: String,
  val evaluationType: String,
  val otherEvaluations: List<EvaluationDTO>
)
