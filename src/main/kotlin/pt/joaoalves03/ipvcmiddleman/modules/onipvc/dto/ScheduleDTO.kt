package pt.joaoalves03.ipvcmiddleman.modules.onipvc.dto

data class ScheduleDto (
  val shortName: String,
  val className: String,
  val classType: String,
  val start: String,
  val end: String,
  val id: String,
  val teachers: List<String>,
  val room: String,
  val statusColor: String,
)