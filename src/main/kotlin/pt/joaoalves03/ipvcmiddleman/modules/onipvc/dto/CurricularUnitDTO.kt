package pt.joaoalves03.ipvcmiddleman.modules.onipvc.dto

import org.springframework.data.redis.core.RedisHash
import java.io.Serializable

@RedisHash
data class CurricularUnitDTO(
  val school: String,
  val schoolYear: String,
  val mainTeacher: NameHours,
  val otherTeachers: List<NameHours>,
  val course: String,
  val name: String,
  val cycle: String,
  val year: Int,
  val semester: String,
  val credits: Int,
  val autonomousWorkHours: Int,

  val shifts: List<NameHours>,
  val summary: String,
  val objectives: List<String>,
  val syllabus: List<NameHours>,
  val teachingMethodologies: String,
  val evaluation: String,
  val mainBibliography: String,
  val complementaryBibliography: String,

  val lastUpdate: String
): Serializable