package pt.joaoalves03.ipvcmiddleman.modules.academicos.dto

data class GradeContainerDTO(
  val average: Float,
  val grades: List<GradeDTO>
)
