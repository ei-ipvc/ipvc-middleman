package pt.joaoalves03.ipvcmiddleman.modules.academicos.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pt.joaoalves03.ipvcmiddleman.modules.academicos.dto.CurricularStatusDTO
import pt.joaoalves03.ipvcmiddleman.modules.academicos.services.CurricularUnitStatusService

@RestController
@RequestMapping("/academicos/curricularUnit")
class CurricularUnitStatusController(val curricularUnitService: CurricularUnitStatusService) {
  @GetMapping("")
  fun curricularUnitStatus(@RequestHeader("x-auth-academicos") cookie: String): ResponseEntity<List<List<CurricularStatusDTO>>> {
    return ResponseEntity.ok(curricularUnitService.getCurricularUnitStatus(cookie))
  }
}