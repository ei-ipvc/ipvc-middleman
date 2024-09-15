package pt.joaoalves03.ipvcmiddleman.modules.onipvc.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import pt.joaoalves03.ipvcmiddleman.modules.onipvc.dto.CurricularUnitDTO
import pt.joaoalves03.ipvcmiddleman.modules.onipvc.services.CurricularUnitService

@RestController
@RequestMapping("/onipvc/curricularUnit")
class CurricularUnitController {
  @GetMapping("")
  fun getCurricularUnitInfo(
    @RequestParam("courseId") courseId: Int,
    @RequestParam("unitId") unitId: Int,
  ): ResponseEntity<CurricularUnitDTO> {
    return ResponseEntity.ok(CurricularUnitService.getCurricularUnitInfo(courseId, unitId))
  }
}