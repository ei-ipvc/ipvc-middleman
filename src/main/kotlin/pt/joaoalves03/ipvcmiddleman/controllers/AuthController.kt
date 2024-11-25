package pt.joaoalves03.ipvcmiddleman.controllers

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import pt.joaoalves03.ipvcmiddleman.dto.AuthorizationDto
import pt.joaoalves03.ipvcmiddleman.dto.AuthorizeDto
import pt.joaoalves03.ipvcmiddleman.modules.onipvc.services.OnIPVCAuthorizationService as OnIPVCAuth
import pt.joaoalves03.ipvcmiddleman.modules.academicos.services.AcademicosAuthorizationService as AcademicosAuth
import pt.joaoalves03.ipvcmiddleman.modules.moodle.services.MoodleAuthorizationService as MoodleAuth
import pt.joaoalves03.ipvcmiddleman.modules.sasocial.services.SASAuthorizationService as SASAuth

@RestController
@Tag(name = "Authentication")
class AuthController(
  val onIPVCAuth: OnIPVCAuth,
  val academicosAuth: AcademicosAuth,
  val moodleAuth: MoodleAuth,
  val sasAuth: SASAuth
) {
  @PostMapping("authorize")
  fun authorize(
    @RequestBody body: AuthorizeDto,
    @RequestParam(required = false) onipvc: Boolean,
    @RequestParam(required = false) academicos: Boolean,
    @RequestParam(required = false) moodle: Boolean,
    @RequestParam(required = false) sasocial: Boolean,
  ) : ResponseEntity<AuthorizationDto> {
    return ResponseEntity.ok(AuthorizationDto(
      onipvc = if (onipvc) onIPVCAuth.getAuthorization(body) else null,
      academicos = if (academicos) academicosAuth.getAuthorization(body) else null,
      moodle = if (moodle) moodleAuth.getAuthorization(body) else null,
      sasocial = if (sasocial) sasAuth.getAuthorization(body, null) else null,
    ))
  }
}