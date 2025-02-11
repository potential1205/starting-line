package com.example.gogoma.data.api

import com.example.gogoma.data.model.MarathonStartInitDataResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface WatchApiService {

    @GET("api/v1/watch/start/marathons/{marathonId}")
    suspend fun startMarathon(
        @Header("Authorization") token: String,
        @Path("marathonId") marathonId: Int
    ): Response<MarathonStartInitDataResponse>
}