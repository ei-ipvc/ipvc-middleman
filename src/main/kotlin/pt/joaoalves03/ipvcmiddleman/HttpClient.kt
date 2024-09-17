package pt.joaoalves03.ipvcmiddleman

import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

object HttpClient {
  val instance: OkHttpClient = OkHttpClient()
    .newBuilder()
    .connectTimeout(30, TimeUnit.SECONDS)
    .readTimeout(30, TimeUnit.SECONDS)
    .build()
}