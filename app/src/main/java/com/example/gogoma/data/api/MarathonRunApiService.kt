package com.example.gogoma.data.api

import com.example.gogoma.data.dto.MarathonReadyDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface MarathonRunApiService {
    @GET("api/v1/watch/start/marathons/{marathonId}/users/{userId}")
    fun startMarathon(
        //@Header("Authorization") token: String,
        @Path("marathonId") marathonId: Int,
        @Path("userId") userId: Int,
    ): Call<MarathonReadyDto>
}
