package pt.joaoalves03.ipvcmiddleman.dto

import com.fasterxml.jackson.annotation.JsonInclude

// Only non-null fields will be sent in the response
@JsonInclude(JsonInclude.Include.NON_NULL)
data class AuthorizationDto (
  var onipvc: String?,
  var academicos: String?,
  var moodle: String?
)