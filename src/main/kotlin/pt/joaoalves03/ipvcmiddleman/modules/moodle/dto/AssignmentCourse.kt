package pt.joaoalves03.ipvcmiddleman.modules.moodle.dto

data class Assignment(
  val id: Int,
  val courseName: String,
  val name: String,
  val dueDate: String,
  val submissionOpenDate: String,
)