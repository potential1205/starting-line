package com.example.myapplication.service

import com.example.myapplication.dto.MarathonReadyDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface MarathonApiService {
    @GET("start/marathons/{marathonId}/users/{userId}")
    fun startMarathon(
        //@Header("Authorization") token: String,
        @Path("marathonId") marathonId: Int,
        @Path("userId") userId: Int,
    ): Call<MarathonReadyDto>
}