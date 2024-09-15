package pt.joaoalves03.ipvcmiddleman.modules.ipvc.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import pt.joaoalves03.ipvcmiddleman.modules.ipvc.dto.TeacherInfoDTO
import pt.joaoalves03.ipvcmiddleman.modules.ipvc.services.TeacherEmailService

@RestController
@RequestMapping("/ipvc/email")
class EmailController {
  @GetMapping("")
  fun getEmail(@RequestParam name: String): ResponseEntity<List<TeacherInfoDTO>> {
    return ResponseEntity.ok(TeacherEmailService.getTeacherEmail(name))
  }
}