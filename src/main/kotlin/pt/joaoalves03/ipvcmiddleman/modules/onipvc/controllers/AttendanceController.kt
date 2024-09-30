package pt.joaoalves03.ipvcmiddleman.modules.onipvc.controllers

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import pt.joaoalves03.ipvcmiddleman.modules.onipvc.dto.AttendanceCourseDto
import pt.joaoalves03.ipvcmiddleman.modules.onipvc.dto.AttendanceDto
import pt.joaoalves03.ipvcmiddleman.modules.onipvc.dto.AttendanceYearsDto
import pt.joaoalves03.ipvcmiddleman.modules.onipvc.services.AttendanceService

@RestController
@RequestMapping("/onipvc/attendance")
@Tag(name = "onIPVC")
class AttendanceController(val attendanceService: AttendanceService) {
  @GetMapping("years")
  fun attendance(@RequestHeader("x-auth-onipvc") cookie: String): ResponseEntity<List<AttendanceYearsDto>> {
    return ResponseEntity.ok(attendanceService.getAvailableYears(cookie))
  }

  @GetMapping("courses")
  fun courses(
    @RequestHeader("x-auth-onipvc") cookie: String,
    @RequestParam("username") username: String,
    @RequestParam("year") year: String
  ): ResponseEntity<List<AttendanceCourseDto>> {
    return ResponseEntity.ok(attendanceService.getAvailableCourses(cookie, username, year))
  }

  @GetMapping("{courseId}/{year}")
  fun attendance(
    @PathVariable("courseId") courseId: String,
    @PathVariable("year") year: String,
    @RequestHeader("x-auth-onipvc") cookie: String
  ): ResponseEntity<List<AttendanceDto>> {
    return ResponseEntity.ok(attendanceService.getAttendance(cookie, courseId, year, null))
  }

  @GetMapping("{courseId}/{year}/{unitId}")
  fun attendanceCourse(
    @PathVariable("courseId") courseId: String,
    @PathVariable("year") year: String,
    @PathVariable("unitId") unitId: String,
    @RequestHeader("x-auth-onipvc") cookie: String,
  ): ResponseEntity<AttendanceDto> {
    val list = attendanceService.getAttendance(cookie, courseId, year, unitId)

    if (list.isEmpty()) {
      return ResponseEntity.notFound().build()
    }

    return ResponseEntity.ok(list[0])
  }
}