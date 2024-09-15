package pt.joaoalves03.ipvcmiddleman.modules.academicos.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pt.joaoalves03.ipvcmiddleman.modules.academicos.dto.TuitionFee
import pt.joaoalves03.ipvcmiddleman.modules.academicos.services.TuitionFeeService

@RestController
@RequestMapping("/academicos/tuitionFees")
class TuitionFeeController {
  @GetMapping("")
  fun getTuitionFees(@RequestHeader("x-auth-academicos") cookie: String) : ResponseEntity<List<TuitionFee>>{
    return ResponseEntity.ok(TuitionFeeService.getTuitionFees(cookie))
  }
}