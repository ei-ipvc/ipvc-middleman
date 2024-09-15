package pt.joaoalves03.ipvcmiddleman.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import pt.joaoalves03.ipvcmiddleman.IncorrectCredentialsException
import pt.joaoalves03.ipvcmiddleman.ServiceUnavailableException
import pt.joaoalves03.ipvcmiddleman.UnauthorizedException

@ControllerAdvice
class ExceptionHandlerController {
  @ExceptionHandler
  fun handleIncorrectCredentialsException(e: IncorrectCredentialsException): ResponseEntity<*> {
    return ResponseEntity.badRequest().body("Incorrect credentials")
  }

  @ExceptionHandler
  fun handleUnauthorizedException(e: UnauthorizedException): ResponseEntity<*> {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("")
  }

  @ExceptionHandler
  fun handleServiceUnavailableException(e: ServiceUnavailableException): ResponseEntity<*> {
    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("")
  }
}