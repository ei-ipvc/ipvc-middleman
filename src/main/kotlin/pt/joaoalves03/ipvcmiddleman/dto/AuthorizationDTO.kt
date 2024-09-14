package pt.joaoalves03.ipvcmiddleman.dto

import com.fasterxml.jackson.annotation.JsonInclude

// Only non-null fields will be sent in the response
@JsonInclude(JsonInclude.Include.NON_NULL)
class AuthorizationDTO {
  var onipvc: String? = null
  var academicos: String? = null
  var moodle: String? = null
}