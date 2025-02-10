package com.example.gogoma.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://i12a808.p.ssafy.io/"

    // MarathonApiService 연결
    val marathonApiService: MarathonApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())  // JSON 파싱을 위한 컨버터
            .build()
            .create(MarathonApiService::class.java)
    }

    // UserApiService 연결
    val userApiService: UserApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserApiService::class.java)
    }

    // FriendApiService 연결
    val friendApiService: FriendApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FriendApiService::class.java)
    }

    val paymentApiService: PaymentApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PaymentApiService::class.java)
    }
}