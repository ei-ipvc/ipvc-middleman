package pt.joaoalves03.ipvcmiddleman.modules.academicos.dto

data class ProfileDTO(
  val name: String,
  val id: String,
  val school: String,
  val courseId: String,
  val courseName: String,
  val image: String
)