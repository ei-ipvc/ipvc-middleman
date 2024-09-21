package pt.joaoalves03.ipvcmiddleman.modules.academicos.controllers

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pt.joaoalves03.ipvcmiddleman.modules.academicos.dto.ProfileDTO
import pt.joaoalves03.ipvcmiddleman.modules.academicos.services.ProfileService

@RestController
@RequestMapping("/academicos/profile")
@Tag(name = "Academicos")
class ProfileController(val profileService: ProfileService) {
  @GetMapping("")
  fun profile(@RequestHeader("x-auth-academicos") cookie: String): ResponseEntity<ProfileDTO> {
    return ResponseEntity.ok(profileService.getProfile(cookie))
  }
}