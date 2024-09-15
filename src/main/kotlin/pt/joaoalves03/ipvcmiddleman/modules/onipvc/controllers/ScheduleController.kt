package pt.joaoalves03.ipvcmiddleman.modules.onipvc.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pt.joaoalves03.ipvcmiddleman.modules.onipvc.dto.ScheduleDTO
import pt.joaoalves03.ipvcmiddleman.modules.onipvc.services.ScheduleService

@RestController
@RequestMapping("/onipvc/schedule")
class ScheduleController {
  @GetMapping("")
  fun schedule(
    @RequestHeader("x-auth-onipvc") cookie: String,
    @RequestParam("year") year: String,
    @RequestParam("semester") semester: String,
    @RequestParam("studentId") studentId: String
  ): ResponseEntity<List<ScheduleDTO>> {
    return ResponseEntity.ok(ScheduleService.getSchedule(cookie, year, semester, studentId))
  }
}