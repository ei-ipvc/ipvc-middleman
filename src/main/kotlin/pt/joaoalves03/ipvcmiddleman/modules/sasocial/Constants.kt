package pt.joaoalves03.ipvcmiddleman.modules.sasocial

object Constants {
  private const val BASE_URL = "https://sasocial.sas.ipvc.pt/api"
  const val AUTHORIZATION_ENDPOINT = "$BASE_URL/authorization/authorize/device-type/WEB"
  const val LOGOUT_ENDPOINT = "$BASE_URL/authorization/authorize/logout/WEB"

  fun mealsEndpoint(canteenId: Int, date: String, period: String): String {
    return "$BASE_URL/alimentation/menu/service/$canteenId/menus/$date/$period"
  }
}