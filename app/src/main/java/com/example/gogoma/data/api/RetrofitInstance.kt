package com.example.gogoma.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * RetrofitInstance는 Retrofit을 사용하여 각 API 서비스 인터페이스를 생성하는 객체입니다.
 * - 각 API 서비스는 Retrofit.Builder를 사용하여 GsonConverterFactory(JSON 파싱을 위한 컨버터)를 추가한 후, API 인터페이스를 생성합니다.
 * - by lazy를 사용하여 실제 사용 시점에 인스턴스가 생성되도록 합니다.
 */

object RetrofitInstance {
    private const val BASE_URL = "https://i12a808.p.ssafy.io/"

    val marathonApiService: MarathonApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())  // JSON 파싱을 위한 컨버터
            .build()
            .create(MarathonApiService::class.java)
    }

    val userApiService: UserApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserApiService::class.java)
    }

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

    val watchApiService: WatchApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WatchApiService::class.java)
    }

    val userMarathonApiService: UserMarathonApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserMarathonApiService::class.java)
    }

}