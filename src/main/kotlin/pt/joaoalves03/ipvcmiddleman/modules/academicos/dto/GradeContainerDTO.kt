package pt.joaoalves03.ipvcmiddleman.modules.academicos.dto

data class GradeContainerDto (
  val average: Float,
  val grades: List<GradeDto>
)
