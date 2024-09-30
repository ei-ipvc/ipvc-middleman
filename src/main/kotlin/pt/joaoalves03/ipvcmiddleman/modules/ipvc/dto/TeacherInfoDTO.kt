package pt.joaoalves03.ipvcmiddleman.modules.ipvc.dto

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import java.io.Serializable

@RedisHash
data class TeacherInfoDto(
  @Id
  val name: String,
  val email: String
): Serializable
