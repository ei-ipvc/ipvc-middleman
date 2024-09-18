package pt.joaoalves03.ipvcmiddleman.modules.academicos.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pt.joaoalves03.ipvcmiddleman.modules.academicos.dto.GradeContainerDTO
import pt.joaoalves03.ipvcmiddleman.modules.academicos.services.GradesService

@RestController
@RequestMapping("/academicos/grades")
class GradesController(val gradesService: GradesService) {
  @GetMapping("")
  fun grades(@RequestHeader("x-auth-academicos") cookie: String): ResponseEntity<GradeContainerDTO> {
    return ResponseEntity.ok(gradesService.getGrades(cookie))
  }
}