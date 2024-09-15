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
    val authorization = AuthorizationDTO()

    if(onipvc) {
      authorization.onipvc = OnIPVCAuth.getAuthorization(body)
    }

    if(academicos) {
      authorization.academicos = AcademicosAuth.getAuthorization(body)
    }

    return ResponseEntity.ok(authorization)
  }
}