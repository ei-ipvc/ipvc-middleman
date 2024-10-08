package pt.joaoalves03.ipvcmiddleman.modules.academicos.dto

import pt.joaoalves03.ipvcmiddleman.modules.moodle.dto.AssignmentDto
import pt.joaoalves03.ipvcmiddleman.modules.onipvc.dto.CurricularUnitDto

data class DetailedCurricularUnitDTO (
  val info: CurricularUnitDto?,
  val summaries: List<String>,
  val assignments: List<AssignmentDto>,
  val evaluations: List<String>
)