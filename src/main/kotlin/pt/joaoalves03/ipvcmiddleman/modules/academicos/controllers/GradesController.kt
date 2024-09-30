package pt.joaoalves03.ipvcmiddleman.modules.academicos.controllers

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pt.joaoalves03.ipvcmiddleman.modules.academicos.dto.GradeContainerDto
import pt.joaoalves03.ipvcmiddleman.modules.academicos.services.GradesService

@RestController
@RequestMapping("/academicos/grades")
@Tag(name = "Academicos")
class GradesController(val gradesService: GradesService) {
  @GetMapping("")
  fun grades(@RequestHeader("x-auth-academicos") cookie: String): ResponseEntity<GradeContainerDto> {
    return ResponseEntity.ok(gradesService.getGrades(cookie))
  }
}