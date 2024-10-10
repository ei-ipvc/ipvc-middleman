package pt.joaoalves03.ipvcmiddleman.modules.onipvc.services

import okhttp3.Request
import org.jsoup.Jsoup
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import pt.joaoalves03.ipvcmiddleman.HttpClient
import pt.joaoalves03.ipvcmiddleman.ServiceUnavailableException
import pt.joaoalves03.ipvcmiddleman.modules.onipvc.Constants
import pt.joaoalves03.ipvcmiddleman.modules.onipvc.dto.CurricularUnitDto
import pt.joaoalves03.ipvcmiddleman.modules.onipvc.dto.NameHoursDto
import java.time.Instant
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit

@Service
class CurricularUnitService(private val redisTemplate: RedisTemplate<String, Any>) {
  private fun parseTeachers(input: String): List<NameHoursDto> {
    val r1 = Regex("[^(]+(?=\\()", RegexOption.IGNORE_CASE)
    val r2 = Regex("\\d+H", RegexOption.IGNORE_CASE)

    val names = r1.findAll(input).map { it.value }.toList()
    val hours = r2.findAll(input).map { it.value.dropLast(1).toFloat() }.toList()

    return names.zip(hours) { name, hour -> NameHoursDto(name, hour) }
  }

  // Enjoy :)
  fun fetchCurricularUnitInfo(courseId: String, unitId: String): CurricularUnitDto {
    val info = getCurricularUnitInfo(unitId)
    if (info != null && ChronoUnit.DAYS.between(
        Instant.parse(info.lastUpdate),
        Instant.now()
      ) < 1
    ) return info

    val request = Request.Builder()
      .url(Constants.curricularUnitInfoEndpoint(courseId, unitId))
      .get()
      .build()

    HttpClient.instance.newCall(request).execute().use { response ->
      if (!response.isSuccessful) throw ServiceUnavailableException()

      val data = Jsoup.parse(response.body!!.string())

      val infoSection = data.selectFirst("#info")!!

      // General info parsing
      val infoSectionLeft = infoSection.selectFirst("div.col-lg-6:nth-child(1)")!!

      // Shift parsing
      val shiftData = infoSection.selectFirst("div.col-lg-6:nth-child(2)")!!
        .selectFirst(".table")!!.select("tr")[1].select("td")

      val shiftNames = shiftData[0].text().split(" ")
      val shiftValues = shiftData[1].text().split(" ")

      println(courseId)

      val shifts = List(shiftNames.size) { index ->
        NameHoursDto(shiftNames[index], shiftValues[index].toFloat())
      }

      // Syllabus parsing
      val syllabus = data.selectFirst("#conteudo")!!.selectFirst(".panel-body")!!
        .wholeText().trim().split("\n")
        .map {
          val elements = it.split(" - ", limit = 2)

          val hours = elements[0].replace("H", "").toFloat()
          val content = elements[1]

          NameHoursDto(content, hours)
        }


      val fetchedInfo = CurricularUnitDto(
        school = infoSectionLeft.select("b:contains(ESCOLA)").first()!!.nextSibling()!!.toString().trim(),
        schoolYear = infoSectionLeft.select("b:contains(ANO LECTIVO)").first()!!.nextSibling()!!.toString().trim(),
        mainTeacher = parseTeachers(
          infoSectionLeft.select("b:contains(DOCENTE RESPONSÁVEL)").first()!!.nextSibling()!!.toString()
            .trim()
        )[0],
        otherTeachers = parseTeachers(
          infoSectionLeft.select("b:contains(OUTROS DOCENTES)").first()!!.nextSibling()!!.toString()
            .trim()
        ),
        course = infoSectionLeft.select("b:contains(CURSO)").first()!!.nextSibling()!!.toString().trim(),
        name = infoSectionLeft.select("b:contains(UNIDADE CURRICULAR)").first()!!.nextSibling()!!.toString().trim(),
        cycle = infoSectionLeft.select("b:contains(CICLO)").first()!!.nextSibling()!!.toString().trim(),
        year = infoSectionLeft.select("b:contains(ANO:)").first()!!.nextSibling()!!.toString().trim().toInt(),
        semester = infoSectionLeft.select("b:contains(SEMESTRE)").first()!!.nextSibling()!!.toString().trim(),
        credits = infoSectionLeft.select("b:contains(ECTS)").first()!!.nextSibling()!!.toString().trim().toInt(),
        autonomousWorkHours = infoSectionLeft.select("b:contains(HORAS TRABALHO AUTÓNOMO)").first()!!.nextSibling()!!
          .toString().trim().toInt(),

        shifts,
        summary = data.selectFirst("#resumo")!!.selectFirst("p")!!.text(),
        objectives = data.selectFirst("#objetivos")!!.selectFirst(".panel-body")!!.html()
          .replace(Regex("<b>.*?</b> "), "\n").trim().split("\n")
          .filter { it != "<br>" },
        syllabus,
        teachingMethodologies = data.selectFirst("#metodologias")!!.selectFirst(".panel-body")!!.text().trim(),
        evaluation = data.selectFirst("#avaliacao")!!.selectFirst(".panel-body")!!.text().trim(),
        mainBibliography = data.selectFirst("#bibliografia")!!.selectFirst(".panel-body")!!.text().trim(),
        complementaryBibliography = data.selectFirst("#bibliografia_comp")!!.selectFirst(".panel-body")!!.text().trim(),

        lastUpdate = java.time.format.DateTimeFormatter.ISO_INSTANT
          .format(Instant.now().atOffset(ZoneOffset.UTC))
      )

      saveCurricularUnitInfo(unitId, fetchedInfo)

      return fetchedInfo
    }
  }

  fun saveCurricularUnitInfo(unitId: String, info: CurricularUnitDto) {
    redisTemplate.opsForValue().set("curricularUnitInfo:${unitId}", info)
  }

  fun getCurricularUnitInfo(unitId: String): CurricularUnitDto? {
    return redisTemplate.opsForValue().get("curricularUnitInfo:${unitId}") as CurricularUnitDto?
  }
}