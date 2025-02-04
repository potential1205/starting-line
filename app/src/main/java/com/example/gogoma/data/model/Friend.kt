package com.example.gogoma.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Friend (
    val rank: Int,
    val id : Int, // 혹시 친구 목록 누르면 해당 친구 상세 페이지로 넘어갈 건가?
    val name : String,
    val profile : String?,
    val totalDistance: Double
) : Parcelable