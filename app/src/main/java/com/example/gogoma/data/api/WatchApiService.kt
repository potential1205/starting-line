package com.example.gogoma.data.api

import com.example.gogoma.data.dto.MarathonReadyDto
import com.example.gogoma.data.model.BooleanResponse
import com.example.gogoma.data.model.MarathonEndInitDataRequest
import com.example.gogoma.data.model.MarathonStartInitDataResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface WatchApiService {
    /*
    userId를 통한 마라톤 준비 초기 데이터 조회(추후 삭제 필요, dto도!)
     */
    @GET("api/v1/watch/start/marathons/{marathonId}/users/{userId}")
    fun getTestMarathonStartInitData(
        //@Header("Authorization") accessToken: String,
        @Path("marathonId") marathonId: Int,
        @Path("userId") userId: Int,
    ): Call<MarathonReadyDto>

    // 마라톤 준비 초기 데이터 조회
    @GET("api/v1/watch/start/marathons/{marathonId}")
    suspend fun getMarathonStartInitData(
        @Header("Authorization") accessToken: String,
        @Path("marathonId") marathonId: Int
    ): Response<MarathonStartInitDataResponse>

    // 마라톤 종료 데이터 업데이트
    @POST("api/v1/watch/end/marathons/{marathonId}")
    fun updateMarathonEndData(
        @Header("Authorization") accessToken: String,
        @Path("marathonId") marathonId: Int,
        @Body request: MarathonEndInitDataRequest
    ): Call<BooleanResponse>
}