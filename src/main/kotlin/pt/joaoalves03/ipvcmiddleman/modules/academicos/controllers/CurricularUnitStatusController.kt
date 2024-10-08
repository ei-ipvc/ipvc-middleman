package pt.joaoalves03.ipvcmiddleman.modules.academicos.controllers

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pt.joaoalves03.ipvcmiddleman.modules.academicos.dto.CurricularUnitStatusDto
import pt.joaoalves03.ipvcmiddleman.modules.academicos.dto.DetailedCurricularUnitDTO
import pt.joaoalves03.ipvcmiddleman.modules.academicos.services.CurricularUnitStatusService
import pt.joaoalves03.ipvcmiddleman.modules.onipvc.services.CurricularUnitService

@RestController
@RequestMapping("/academicos/curricularUnit")
@Tag(name = "Academicos")
class CurricularUnitStatusController(
  val statusService: CurricularUnitStatusService,
  val detailsService: CurricularUnitService
) {
  @GetMapping("")
  fun status(@RequestHeader("x-auth-academicos") cookie: String): ResponseEntity<CurricularUnitStatusDto> {
    return ResponseEntity.ok(statusService.getStatus(cookie))
  }

  @GetMapping("/{courseId}/{id}")
  fun details(
    @RequestHeader("x-auth-academicos") cookie: String,
    @PathVariable courseId: Int,
    @PathVariable id: Int
  ): ResponseEntity<DetailedCurricularUnitDTO> {
    return ResponseEntity.ok(DetailedCurricularUnitDTO(
      info = detailsService.fetchCurricularUnitInfo(courseId, id),
      summaries = emptyList(),
      assignments = emptyList(),
      evaluations = emptyList()
    ))
  }
}