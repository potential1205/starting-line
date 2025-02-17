package com.example.gogoma.data.api

import com.example.gogoma.data.model.BooleanResponse
import com.example.gogoma.data.model.CreateUserRequest
import com.example.gogoma.data.model.FriendResponse
import com.example.gogoma.data.model.KakaoClientOauthTokenResponse
import com.example.gogoma.data.model.KakaoUserInfo
import com.example.gogoma.data.model.StatusResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApiService {

    //카카오 인가코드 받아오기
    @GET("api/v1/users/kakao/login")
    suspend fun redirectToKakaoLogin(
        @Header("Authorization") accessToken: String
    ) : Response<ResponseBody>

    //카카오 콜백 함수
    @GET("api/v1/users/kakao/callback")
    suspend fun handleKakaoCallback(
        @Query("code") code: String
    ): Response<KakaoClientOauthTokenResponse>

    @POST("api/v1/users/kakao/refresh")
    suspend fun refreshAccessToken(
        @Header("Authorization") refreshToken: String
    ): Response<KakaoClientOauthTokenResponse>

    // 로그인/회원가입 판단
    @GET("api/v1/users/auth/check")
    suspend fun determineLoginOrSignUp(
        @Header("Authorization") accessToken: String
    ): Response<StatusResponse>

    //꼬마 서비스 회원가입
    @POST("api/v1/users/signup")
    suspend fun signup(
        @Body request: CreateUserRequest
    ): Response<BooleanResponse>

    //꼬마 서비스 로그인
    @POST("api/v1/users/login")
    suspend fun login(
        @Header("Authorization") accessToken: String
    ): Response<BooleanResponse>

    // 카카오 User 정보 조회 API
    @GET("api/v1/users/kakao/userinfo")
    suspend fun getKakaoUserInfo(
        @Header("Authorization") accessToken: String
    ): Response<KakaoUserInfo>

    // 회원 삭제
    @DELETE("api/v1/users")
    suspend fun deleteUserByID(
        @Header("Authorization") accessToken: String
    ): Response<BooleanResponse>

    // 카카오 연결 해제
    @DELETE("api/v1/users/kakao/unlink")
    suspend fun unlinkKakao(
        @Header("Authorization") accessToken: String
    ): Response<BooleanResponse>

    // 내가 참여한 대회 친구 목록
    @GET("api/v1/users/upcoming/friends")
    suspend fun getUpcomingMarathonFriendList(
        @Header("Authorization") accessToken: String
    ): Response<FriendResponse>

    // 대회 신청 시 친구들에게 알림 보내기
    @POST("api/v1/users/alert/{marathonId}")
    fun sendPushNotification(
        @Header("Authorization") accessToken: String,
        @Path("marathonId") marathonId: Int
    ): Response<BooleanResponse>
}