package pt.joaoalves03.ipvcmiddleman.modules.onipvc

object Constants {
  const val CD_COURSE = "https://on.ipvc.pt/v1/modulos/atividadeletiva/alunos_mapa_assiduidade_select_curso.php"
  const val LOGIN_ENDPOINT = "https://on.ipvc.pt/login.php"
  fun attendanceEndpoint(course: String): String {
    return "https://on.ipvc.pt/v1/modulos/atividadeletiva/alunos_mapa_assiduidade_grelha.php?cd_curso=$course&cd_letivo=202324"
  }
  const val ATTENDANCE_YEARS = "https://on.ipvc.pt/v1/modulos/atividadeletiva/alunos_mapa_assiduidade.php"
  const val ATTENDANCE_COURSES = "https://on.ipvc.pt/v1/modulos/atividadeletiva/alunos_mapa_assiduidade_select_curso.php"
}
