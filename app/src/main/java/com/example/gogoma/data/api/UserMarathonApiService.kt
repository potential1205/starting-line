package com.example.gogoma.data.api

import com.example.gogoma.data.model.UserMarathonDetailResponse
import com.example.gogoma.data.model.UserMarathonSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface UserMarathonApiService {

    @GET("api/v1/usermarathons")
    suspend fun getUserMarathonList(
        @Header("Authorization") accessToken: String
    ): Response<UserMarathonSearchResponse>

    @GET("api/v1/usermarthons/{id}")
    suspend fun getUserMarathonById(
        @Header("Authorization") accessToken: String,
        @Path("id") marathonId: Int
    ): Response<UserMarathonDetailResponse>
}