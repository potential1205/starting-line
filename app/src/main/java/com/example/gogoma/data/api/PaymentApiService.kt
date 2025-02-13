package com.example.gogoma.data.api

import com.example.gogoma.data.dto.KakaoPayApproveRequest
import com.example.gogoma.data.dto.KakaoPayApproveResponse
import com.example.gogoma.data.dto.KakaoPayReadyRequest
import com.example.gogoma.data.dto.KakaoPayReadyResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface PaymentApiService {
    @POST("api/v1/usermarathons/pay/kakao/ready")
    suspend fun requestKakaoPayReady(
        @Header("Authorization") accessToken: String,
        @Body request: KakaoPayReadyRequest
    ): Response<KakaoPayReadyResponse>

    @POST("api/v1/usermarathons/pay/kakao/approve")
    suspend fun requestKakaoPayApprove(
        @Header("Authorization") accessToken: String,
        @Body request: KakaoPayApproveRequest
    ): Response<KakaoPayApproveResponse>

    @GET("api/v1/usermarathons/pay/kakao/redirect")
    suspend fun redirectAfterPayment(
        @Query("pg_token") pgToken: String,
        @Query("redirect") redirect: String
    ): Response<Void>
}