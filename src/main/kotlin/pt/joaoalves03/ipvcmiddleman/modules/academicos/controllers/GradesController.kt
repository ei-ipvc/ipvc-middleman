package pt.joaoalves03.ipvcmiddleman.modules.academicos.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pt.joaoalves03.ipvcmiddleman.modules.academicos.dto.GradeDTO
import pt.joaoalves03.ipvcmiddleman.modules.academicos.services.GradesService

@RestController
@RequestMapping("/academicos/grades")
class GradesController {
  @GetMapping("")
  fun grades(@RequestHeader("x-auth-academicos") cookie: String): ResponseEntity<List<GradeDTO>> {
    return ResponseEntity.ok(GradesService.getGrades(cookie))
  }
}