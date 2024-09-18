package pt.joaoalves03.ipvcmiddleman.modules.academicos.dto

data class GradeDTO(
  val evaluationDate: String,
  val curricularUnitId: String,
  val schoolYear: String,
  val credits: Int,
  val semester: Int,
  val curricularUnitName: String,
  val finalGrade: Float,
  val curricularUnitState: String,
  val evaluationType: String,
)
