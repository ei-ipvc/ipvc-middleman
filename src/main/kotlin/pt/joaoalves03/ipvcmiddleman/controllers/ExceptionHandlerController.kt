package pt.joaoalves03.ipvcmiddleman.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import pt.joaoalves03.ipvcmiddleman.IncorrectCredentialsException

@ControllerAdvice
class ExceptionHandlerController {
  @ExceptionHandler
  fun handleIncorrectCredentialsException(e: IncorrectCredentialsException): ResponseEntity<*> {
    return ResponseEntity.badRequest().body("Incorrect credentials")
  }
}