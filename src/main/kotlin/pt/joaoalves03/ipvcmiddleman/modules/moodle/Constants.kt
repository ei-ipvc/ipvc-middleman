package pt.joaoalves03.ipvcmiddleman.modules.moodle

object Constants {
  fun loginEndpoint(username: String, password: String): String {
    return "https://elearning.ipvc.pt/ipvc2024/login/token.php?username=$username&password=$password&service=moodle_mobile_app"
  }
  fun assignmentsEndpoint(token: String): String {
    return "https://elearning.ipvc.pt/ipvc2023/webservice/rest/server.php?wstoken=$token&moodlewsrestformat=json&wsfunction=mod_assign_get_assignments"
  }
}