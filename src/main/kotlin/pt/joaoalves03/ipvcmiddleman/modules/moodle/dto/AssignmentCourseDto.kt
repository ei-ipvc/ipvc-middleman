package pt.joaoalves03.ipvcmiddleman.modules.moodle.dto

data class AssignmentDto (
  val id: Int,
  val courseName: String,
  val name: String,
  val dueDate: String,
  val submissionOpenDate: String,
)