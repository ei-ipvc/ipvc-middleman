package pt.joaoalves03.ipvcmiddleman.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import pt.joaoalves03.ipvcmiddleman.dto.AuthorizationDTO
import pt.joaoalves03.ipvcmiddleman.dto.AuthorizeDTO
import pt.joaoalves03.ipvcmiddleman.modules.onipvc.services.OnIPVCAuthorizationService as OnIPVCAuth
import pt.joaoalves03.ipvcmiddleman.modules.academicos.services.AcademicosAuthorizationService as AcademicosAuth
import pt.joaoalves03.ipvcmiddleman.modules.moodle.services.MoodleAuthorizationService as MoodleAuth

@RestController
class AuthController(
  val onIPVCAuth: OnIPVCAuth,
  val academicosAuth: AcademicosAuth,
  val moodleAuth: MoodleAuth
) {
  @PostMapping("authorize")
  fun authorize(
    @RequestBody body: AuthorizeDTO,
    @RequestParam(required = false) onipvc: Boolean,
    @RequestParam(required = false) academicos: Boolean,
    @RequestParam(required = false) moodle: Boolean,
  ) : ResponseEntity<AuthorizationDTO> {
    return ResponseEntity.ok(AuthorizationDTO(
      onipvc = if (onipvc) onIPVCAuth.getAuthorization(body) else null,
      academicos = if (academicos) academicosAuth.getAuthorization(body) else null,
      moodle = if (moodle) moodleAuth.getAuthorization(body) else null
    ))
  }
}