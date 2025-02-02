package com.example.gogoma.data.api

import com.example.gogoma.data.model.MarathonDetailResponse
import com.example.gogoma.data.model.MarathonSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MarathonApiService {

    @GET("api/v1/marathons")
    suspend fun getMarathons(
        @Query("marathonStatus") marathonStatus: String?,
        @Query("city") city: String?,
        @Query("year") year: String?,
        @Query("month") month: String?,
        @Query("courseTypeList") courseTypeList: List<String>?,
        @Query("keyword") keyword: String?
    ): Response<MarathonSearchResponse>

    @GET("api/v1/marathons/{id}")
    suspend fun getMarathonById(
        @Path("id") marathonId: Int,
    ): Response<MarathonDetailResponse>

}