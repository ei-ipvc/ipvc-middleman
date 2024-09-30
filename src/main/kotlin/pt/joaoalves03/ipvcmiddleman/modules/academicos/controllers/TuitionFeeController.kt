package pt.joaoalves03.ipvcmiddleman.modules.academicos.controllers

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pt.joaoalves03.ipvcmiddleman.modules.academicos.dto.TuitionFeeDto
import pt.joaoalves03.ipvcmiddleman.modules.academicos.services.TuitionFeeService

@RestController
@RequestMapping("/academicos/tuitionFees")
@Tag(name = "Academicos")
class TuitionFeeController(private val tuitionFeeService: TuitionFeeService) {
  @GetMapping("")
  fun getTuitionFees(@RequestHeader("x-auth-academicos") cookie: String) : ResponseEntity<List<TuitionFeeDto>>{
    return ResponseEntity.ok(tuitionFeeService.getTuitionFees(cookie))
  }
}