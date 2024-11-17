package pt.joaoalves03.ipvcmiddleman

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.info.GitProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import java.util.*


@SpringBootApplication
@EnableScheduling
class IpvcMiddleman

fun main(args: Array<String>) {
  runApplication<IpvcMiddleman>(*args)
}

@Configuration
class GitConfig {
  @Bean
  fun gitProperties(): GitProperties {
    val props = Properties().apply {
      val process = ProcessBuilder("git", "rev-parse", "--short", "HEAD")
        .redirectError(ProcessBuilder.Redirect.INHERIT)
        .start()

      val commitHash = process.inputStream.bufferedReader().readText().trim()

      setProperty("git.commit.id.abbrev", commitHash)
    }

    return GitProperties(props)
  }
}