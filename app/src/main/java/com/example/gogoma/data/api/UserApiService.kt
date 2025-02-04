package com.example.gogoma.data.api

import com.example.gogoma.data.model.CreateUserRequest
import com.example.gogoma.data.model.CreateUserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApiService {

    //카카오 회원가입
    @POST("api/v1/users/signup")
    suspend fun signupKakao(
        @Body request: CreateUserRequest
    ): Response<CreateUserResponse>

}