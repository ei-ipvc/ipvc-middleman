package pt.joaoalves03.ipvcmiddleman.modules.onipvc.controllers

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pt.joaoalves03.ipvcmiddleman.modules.onipvc.dto.ScheduleDto
import pt.joaoalves03.ipvcmiddleman.modules.onipvc.services.ScheduleService

@RestController
@RequestMapping("/onipvc/schedule")
@Tag(name = "onIPVC")
class ScheduleController(private val scheduleService: ScheduleService) {
  @GetMapping("")
  fun schedule(
    @RequestHeader("x-auth-onipvc") cookie: String,
    @RequestParam("year") year: String,
    @RequestParam("semester") semester: String,
    @RequestParam("studentId") studentId: String
  ): ResponseEntity<List<ScheduleDto>> {
    return ResponseEntity.ok(scheduleService.getSchedule(cookie, year, semester, studentId))
  }
}