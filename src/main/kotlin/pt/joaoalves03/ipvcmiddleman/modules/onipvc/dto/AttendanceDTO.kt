package pt.joaoalves03.ipvcmiddleman.modules.onipvc.dto

data class AttendanceDTO(
  val subjectId: String,
  val subjectName: String,
  val classType: String,
  val classIdentifier: String,
  val hoursAttended: String,
  val hoursLectured: String,
  val attendance: String,
)