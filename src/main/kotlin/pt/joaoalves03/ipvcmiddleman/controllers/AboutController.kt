package pt.joaoalves03.ipvcmiddleman.controllers

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.boot.info.GitProperties
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import pt.joaoalves03.ipvcmiddleman.dto.AboutDTO

@RestController
@Tag(name = "General")
class AboutController(private val gitProperties: GitProperties) {
  @GetMapping("")
  fun about() : ResponseEntity<AboutDTO> {
    val shortCommitHash = gitProperties.get("git.commit.id.abbrev")

    return ResponseEntity.ok(
      AboutDTO(
        System.getenv("SERVER_NAME") ?: "Middleman server",
        System.getenv("SERVER_VERSION")
          ?: if (shortCommitHash == null)
            "unknown"
            else "dev-${shortCommitHash}"
      )
    )
  }
}