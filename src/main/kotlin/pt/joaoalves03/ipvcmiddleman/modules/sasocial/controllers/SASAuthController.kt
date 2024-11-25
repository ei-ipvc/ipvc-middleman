package pt.joaoalves03.ipvcmiddleman.modules.sasocial.controllers

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pt.joaoalves03.ipvcmiddleman.modules.sasocial.services.SASAuthorizationService

@RestController
@RequestMapping("/sasocial")
@Tag(name = "SAS")
class SASAuthController(val sasAuth: SASAuthorizationService) {
  @GetMapping("/token")
  fun getToken(@RequestHeader("x-auth-sas-refresh") refreshToken: String): ResponseEntity<String> {
    return ResponseEntity.ok(sasAuth.getAuthorization(null, refreshToken).accessToken)
  }

  @GetMapping("/logout")
  fun logout(@RequestHeader("x-auth-sas-refresh") refreshToken: String): ResponseEntity<String> {
    sasAuth.logout(refreshToken)
    return ResponseEntity.ok("")
  }
}