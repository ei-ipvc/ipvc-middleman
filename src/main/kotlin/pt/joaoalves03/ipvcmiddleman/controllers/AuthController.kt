package pt.joaoalves03.ipvcmiddleman.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import pt.joaoalves03.ipvcmiddleman.dto.AuthorizationDTO
import pt.joaoalves03.ipvcmiddleman.dto.AuthorizeDTO
import pt.joaoalves03.ipvcmiddleman.modules.onipvc.services.AuthorizationService as OnIPVCAuth
import pt.joaoalves03.ipvcmiddleman.modules.academicos.services.AuthorizationService as AcademicosAuth

@RestController
class AuthController {
  @PostMapping("authorize")
  fun authorize(
    @RequestBody body: AuthorizeDTO,
    @RequestParam(required = false) onipvc: Boolean,
    @RequestParam(required = false) academicos: Boolean
  ) : ResponseEntity<AuthorizationDTO> {
    return ResponseEntity.ok(AuthorizationDTO(
      onipvc = if (onipvc) OnIPVCAuth.getAuthorization(body) else null,
      academicos = if (academicos) AcademicosAuth.getAuthorization(body) else null,
      moodle = null
    ))
  }
}