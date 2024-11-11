package pt.joaoalves03.ipvcmiddleman.modules.academicos.dto

data class ProfileDto(
  val name: String,
  val id: String,
  val school: String,
  val acronym: String,
  val courseId: String,
  val courseName: String,
  val courseAcronym: String,
  val image: String
)