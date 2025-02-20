package com.example.gogoma.data.api

import com.example.gogoma.data.model.BooleanResponse
import com.example.gogoma.data.model.FriendListResponse
import com.example.gogoma.data.model.FriendResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface FriendApiService {

    // accessToken 사용해 친구 목록 누적 거리 순으로 조회
    @GET("api/v1/users/friends")
    suspend fun getFriends(
        @Header("Authorization") accessToken: String?
    ): Response<FriendListResponse>

    // accessToken 사용해 친구 목록 갱신
    @POST("/api/v1/users/update/friend")
    suspend fun updateFriend(
        @Header("Authorization") accessToken: String?
    ): Response<BooleanResponse>
}