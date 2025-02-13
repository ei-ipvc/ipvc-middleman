package pt.joaoalves03.ipvcmiddleman.modules.onipvc.controllers

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pt.joaoalves03.ipvcmiddleman.UnauthorizedException
import pt.joaoalves03.ipvcmiddleman.modules.onipvc.dto.ManualScheduleInitialOptionsDTO
import pt.joaoalves03.ipvcmiddleman.modules.onipvc.dto.OptionPair
import pt.joaoalves03.ipvcmiddleman.modules.onipvc.dto.ScheduleDto
import pt.joaoalves03.ipvcmiddleman.modules.onipvc.services.ScheduleService

@RestController
@RequestMapping("/onipvc/schedule")
@Tag(name = "onIPVC")
class ScheduleController(private val scheduleService: ScheduleService) {
  @GetMapping("")
  fun schedule(
    @RequestHeader("x-auth-onipvc") cookie: String,
    @RequestParam("year", required = false) year: String?,
    @RequestParam("semester", required = false) semester: String?,
    @RequestParam("studentId") studentId: String
  ): ResponseEntity<List<ScheduleDto>> {
    return ResponseEntity.ok(scheduleService.getSchedule(cookie, year ?: "", semester ?: "", studentId))
  }

  @GetMapping("/manualOptions")
  fun getManualOptions(
    @RequestHeader("x-auth-onipvc") cookie: String,
  ): ResponseEntity<ManualScheduleInitialOptionsDTO> {
    return try {
      ResponseEntity.ok(scheduleService.getManualScheduleInitialOptions(cookie))
    } catch (ex: UnauthorizedException) {
      ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
    }
  }

  @GetMapping("/courses")
  fun getCourseList(
    @RequestHeader("x-auth-onipvc") cookie: String,
    @RequestParam("year") year: String,
    @RequestParam("degree") degree: String,
    @RequestParam("school") school: String,
  ): ResponseEntity<List<OptionPair<String, String>>> {
    return try {
      ResponseEntity.ok(scheduleService.getCourseList(cookie, year, degree, school))
    } catch (ex: UnauthorizedException) {
      ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
    }
  }

//  @GetMapping("/weeks")
//  fun getWeekList(
//    @RequestHeader("x-auth-onipvc") cookie: String,
//    @RequestParam("year") year: String,
//    @RequestParam("semester") semester: String,
//  ): ResponseEntity<List<OptionPair<String, String>>> {
//    return try {
//      ResponseEntity.ok(scheduleService.getWeekList(cookie, year, semester))
//    } catch (ex: UnauthorizedException) {
//      ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
//    }
//  }

  @GetMapping("/classes")
  fun getClassList(
    @RequestHeader("x-auth-onipvc") cookie: String,
    @RequestParam("year") year: String,
    @RequestParam("semester") semester: String,
    @RequestParam("course") course: String,
  ): ResponseEntity<List<OptionPair<String, String>>> {
    return try {
      ResponseEntity.ok(scheduleService.getClassList(cookie, year, semester, course))
    } catch (ex: UnauthorizedException) {
      ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
    }
  }

  @GetMapping("/manual/{year}/{semester}/{classId}")
  fun manualSchedule(
    @RequestHeader("x-auth-onipvc") cookie: String,
    @PathVariable("year") year: String,
    @PathVariable("semester") semester: String,
    @PathVariable("classId") classId: String
  ): ResponseEntity<List<ScheduleDto>> {
    return ResponseEntity.ok(scheduleService.getManualSchedule(cookie, year, semester, classId))
  }
}