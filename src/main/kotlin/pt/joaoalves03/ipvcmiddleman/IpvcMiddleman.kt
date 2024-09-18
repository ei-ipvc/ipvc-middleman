package pt.joaoalves03.ipvcmiddleman

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class IpvcMiddleman

fun main(args: Array<String>) {
  runApplication<IpvcMiddleman>(*args)
}
