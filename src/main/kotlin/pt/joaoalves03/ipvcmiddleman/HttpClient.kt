package pt.joaoalves03.ipvcmiddleman

import okhttp3.OkHttpClient

object HttpClient {
  val instance: OkHttpClient = OkHttpClient()
}