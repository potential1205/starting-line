package com.example.gogoma.data.api

import com.example.gogoma.data.model.Friend
import retrofit2.http.GET
import retrofit2.http.Header

interface FriendApiService {

    // accessToken 사용해 친구 목록 누적 거리 순으로 조회
    @GET("api/v1/users/friends")
    suspend fun getFriends(
        @Header("Authorization") accessToken: String?
    ): List<Friend>
}