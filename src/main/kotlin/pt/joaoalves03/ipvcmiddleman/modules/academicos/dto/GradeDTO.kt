package pt.joaoalves03.ipvcmiddleman.modules.academicos.dto

data class GradeDTO(
  val evaluationDate: String?,
  val curricularUnitId: String,
  val schoolYear: String,
  val credits: String,
  val semester: String,
  val curricularUnitName: String,
  val finalGrade: String?,
  val curricularUnitState: String,
  val evaluationType: String?,
)
