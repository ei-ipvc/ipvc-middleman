package pt.joaoalves03.ipvcmiddleman.modules.onipvc.controllers

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import pt.joaoalves03.ipvcmiddleman.modules.onipvc.dto.CurricularUnitDto
import pt.joaoalves03.ipvcmiddleman.modules.onipvc.services.CurricularUnitService

@RestController
@RequestMapping("/onipvc/curricularUnit")
@Tag(name = "onIPVC")
class CurricularUnitController(
  private val curricularUnitService: CurricularUnitService,
) {
  @GetMapping("")
  fun getCurricularUnitInfo(
    @RequestParam("courseId") courseId: Int,
    @RequestParam("unitId") unitId: Int,
  ): ResponseEntity<CurricularUnitDto> {
    return ResponseEntity.ok(curricularUnitService.fetchCurricularUnitInfo(courseId, unitId))
  }
}