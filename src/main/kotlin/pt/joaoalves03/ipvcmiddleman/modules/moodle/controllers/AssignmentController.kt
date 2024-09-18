package pt.joaoalves03.ipvcmiddleman.modules.moodle.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pt.joaoalves03.ipvcmiddleman.modules.moodle.dto.Assignment
import pt.joaoalves03.ipvcmiddleman.modules.moodle.services.AssignmentsService

@RestController
@RequestMapping("/moodle/assignments")
class AssignmentController(val assignmentsService: AssignmentsService) {
  @GetMapping("")
  fun getAssignments(@RequestHeader("x-auth-moodle") token: String): ResponseEntity<List<Assignment>> {
    return ResponseEntity.ok(assignmentsService.getAssignments(token))
  }
}