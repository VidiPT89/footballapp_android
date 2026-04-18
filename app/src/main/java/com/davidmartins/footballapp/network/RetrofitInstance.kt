package com.davidmartins.footballapp.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Singleton para Retrofit
object RetrofitInstance {

    private const val API_TOKEN = "43809d0b74814d279fa9c483dd588211"

    val api: FootballApiService by lazy {
        val httpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("X-Auth-Token", API_TOKEN)
                    .build()
                chain.proceed(request)
            }
            .build()

        Retrofit.Builder()
            .baseUrl("https://api.football-data.org/v4/")
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FootballApiService::class.java)
    }
}
