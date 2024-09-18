package pt.joaoalves03.ipvcmiddleman

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.scheduling.config.ScheduledTask
import org.springframework.stereotype.Component
import pt.joaoalves03.ipvcmiddleman.modules.ipvc.services.TeacherEmailService

@Component
class ScheduledTasks(private val teacherEmailService: TeacherEmailService) {
  private val logger: Logger = LoggerFactory.getLogger(ScheduledTask::class.java)

  @Scheduled(fixedRate = 1000 * 60 * 60 * 24)
  fun updateEmails() {
    logger.info("Updating emails")
    teacherEmailService.fetchTeacherInfo()
    logger.info("Email update complete")
  }
}