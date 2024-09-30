package pt.joaoalves03.ipvcmiddleman.modules.ipvc.controllers

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import pt.joaoalves03.ipvcmiddleman.modules.ipvc.dto.TeacherInfoDto
import pt.joaoalves03.ipvcmiddleman.modules.ipvc.services.TeacherEmailService

@RestController
@RequestMapping("/ipvc/email")
@Tag(name = "IPVC")
class EmailController(private val teacherEmailService: TeacherEmailService) {
  @GetMapping("")
  fun getEmail(@RequestParam name: String): ResponseEntity<List<TeacherInfoDto>> {
    return ResponseEntity.ok(teacherEmailService.getTeacherInfoListByName(name))
  }
}