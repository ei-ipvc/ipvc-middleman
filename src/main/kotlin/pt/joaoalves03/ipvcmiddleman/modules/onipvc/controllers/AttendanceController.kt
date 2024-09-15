package pt.joaoalves03.ipvcmiddleman.modules.onipvc.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import pt.joaoalves03.ipvcmiddleman.modules.onipvc.dto.AttendanceCourseDTO
import pt.joaoalves03.ipvcmiddleman.modules.onipvc.dto.AttendanceDTO
import pt.joaoalves03.ipvcmiddleman.modules.onipvc.dto.AttendanceYearsDTO
import pt.joaoalves03.ipvcmiddleman.modules.onipvc.services.AttendanceService

@RestController
@RequestMapping("/onipvc/attendance")
class AttendanceController {
  @GetMapping("years")
  fun attendance(@RequestHeader("x-auth-onipvc") cookie: String): ResponseEntity<List<AttendanceYearsDTO>> {
    return ResponseEntity.ok(AttendanceService.getAvailableYears(cookie))
  }

  @GetMapping("courses")
  fun courses(
    @RequestHeader("x-auth-onipvc") cookie: String,
    @RequestParam("username") username: String,
    @RequestParam("year") year: String
  ): ResponseEntity<List<AttendanceCourseDTO>> {
    return ResponseEntity.ok(AttendanceService.getAvailableCourses(cookie, username, year))
  }

  @GetMapping("")
  fun attendance(
    @RequestHeader("x-auth-onipvc") cookie: String,
    @RequestParam("courseId") courseId: String,
    @RequestParam("year") year: String
  ): ResponseEntity<List<AttendanceDTO>> {
    return ResponseEntity.ok(AttendanceService.getAttendance(cookie, courseId, year))
  }
}