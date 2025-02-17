package com.example.gogoma.data.api

import com.example.gogoma.data.dto.UpdateUserMarathonRequest
import com.example.gogoma.data.model.BooleanResponse
import com.example.gogoma.data.model.CreateUserMarathonRequest
import com.example.gogoma.data.model.UserMarathonDetailResponse
import com.example.gogoma.data.model.UserMarathonSearchResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface UserMarathonApiService {

    @GET("api/v1/usermarathons")
    suspend fun getUserMarathonList(
        @Header("Authorization") accessToken: String
    ): Response<UserMarathonSearchResponse>

    @POST("api/v1/usermarathons")
    suspend fun registerUserMarathon(
        @Header("Authorization") accessToken: String,
        @Body request: CreateUserMarathonRequest
    ): BooleanResponse

    @GET("api/v1/usermarathons/{id}")
    suspend fun getUserMarathonById(
        @Header("Authorization") accessToken: String,
        @Path("id") id: Int
    ): Response<UserMarathonDetailResponse>

    @PATCH("api/v1/usermarathons/{id}")
    suspend fun updateUserMarathon(
        @Header("Authorization") accessToken: String,
        @Path("id") marathonId: Int,
        @Body request: UpdateUserMarathonRequest
    ): BooleanResponse

    @GET("api/v1/usermarathons/duplicate/{marathonId}")
    suspend fun checkDuplicateUserMarathon(
        @Header("Authorization") accessToken: String,
        @Path("marathonId") marathonid: Int
    ): BooleanResponse
}