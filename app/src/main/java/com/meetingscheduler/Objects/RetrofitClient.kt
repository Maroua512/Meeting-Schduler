package com.meetingscheduler.Objects

import com.meetingscheduler.Interfaces.MailgunService
import okhttp3.OkHttpClient

import okhttp3.Credentials
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://api.mailgun.net/v3/"
    private const val API_KEY = "7dcb1c19f55311199a15b6fe3dccc7eb-7a3af442-83b2eea9" // Remplacez par votre clé API

    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val originalRequest = chain.request()
            val authenticatedRequest = originalRequest.newBuilder()
                .header("Authorization", Credentials.basic("api", API_KEY))
                .build()
            chain.proceed(authenticatedRequest)
        }
        .build()

    val instance: MailgunService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client) // Utiliser OkHttpClient avec Retrofit
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(MailgunService::class.java)
    }
}
