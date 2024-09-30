package pt.joaoalves03.ipvcmiddleman.modules.moodle.services

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.Request
import org.springframework.stereotype.Service
import pt.joaoalves03.ipvcmiddleman.HttpClient
import pt.joaoalves03.ipvcmiddleman.UnauthorizedException
import pt.joaoalves03.ipvcmiddleman.modules.moodle.Constants
import pt.joaoalves03.ipvcmiddleman.modules.moodle.dto.AssignmentDto

@Service
class AssignmentsService {
  private val mapper = jacksonObjectMapper()

  fun getAssignments(token: String): List<AssignmentDto> {
    val request = Request.Builder()
      .url(Constants.assignmentsEndpoint(token))
      .get()
      .build()

    HttpClient.instance.newCall(request).execute().use { response ->
      val data = response.body!!.string()

      if (data.contains("invalidlogin")) throw UnauthorizedException()

      val res = mapper.readTree(data)

      val assignments: MutableList<AssignmentDto> = ArrayList()

      res["courses"].forEach { unit ->
        unit["assignments"].forEach { assignment ->
          assignments.add(
            AssignmentDto(
              assignment["id"].asText().toInt(),
              unit["fullname"].asText(),
              assignment["name"].asText(),
              java.time.format.DateTimeFormatter.ISO_INSTANT
                .format(java.time.Instant.ofEpochSecond(assignment["duedate"].asText().toLong())),
              java.time.format.DateTimeFormatter.ISO_INSTANT
                .format(java.time.Instant.ofEpochSecond(assignment["allowsubmissionsfromdate"].asText().toLong())),
            )
          )
        }
      }

      return assignments
    }
  }
}