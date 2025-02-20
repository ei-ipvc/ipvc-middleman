package pt.joaoalves03.ipvcmiddleman.modules.onipvc

object Constants {
  const val CD_COURSE = "https://on.ipvc.pt/v1/modulos/atividadeletiva/alunos_mapa_assiduidade_select_curso.php"
  const val LOGIN_ENDPOINT = "https://on.ipvc.pt/login.php"
  fun attendanceEndpoint(course: String, year: String): String {
    return "https://on.ipvc.pt/v1/modulos/atividadeletiva/alunos_mapa_assiduidade_grelha.php?cd_curso=$course&cd_letivo=$year"
  }
  const val ATTENDANCE_YEARS = "https://on.ipvc.pt/v1/modulos/atividadeletiva/alunos_mapa_assiduidade.php"
  const val ATTENDANCE_COURSES = "https://on.ipvc.pt/v1/modulos/atividadeletiva/alunos_mapa_assiduidade_select_curso.php"
  const val SCHEDULE_ENDPOINT = "https://on.ipvc.pt/v1/modulos/atividadeletiva/horario_source_v3.php"
  fun curricularUnitInfoEndpoint(courseId: String, unitId: String): String {
    return "https://on.ipvc.pt/v1/puc.php?cd_curso=$courseId&cd_discip=$unitId&lang=pt"
  }
  const val MANUAL_SCHEDULE_OPTIONS_ENDPOINT = "https://on.ipvc.pt/v1/modulos/atividadeletiva/horarios_consulta.php"
  const val COURSE_LIST_ENDPOINT = "https://on.ipvc.pt/v1/modulos/atividadeletiva/source_select_cursosH.php"
  const val WEEK_LIST_ENDPOINT = "https://on.ipvc.pt/v1/modulos/atividadeletiva/source_select_semanasH.php"
  const val CLASS_LIST_ENDPOINT = "https://on.ipvc.pt/v1/modulos/atividadeletiva/source_select_turmasH.php"
}
