package com.example.gogoma.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "http://54.180.135.126/"

    // Retrofit 인스턴스를 생성해서 반환
    val apiService: MarathonApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())  // JSON 파싱을 위한 컨버터
            .build()
            .create(MarathonApiService::class.java)
    }
}