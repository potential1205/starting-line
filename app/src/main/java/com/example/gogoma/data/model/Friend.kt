package com.example.gogoma.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Friend(
    val friendId: Int,
    val friendName: String,
    var currentDistance: Int,       // cm 단위
    var currentDistanceRate: Float,
    var isMe: Boolean,
    var rank: Int
):Parcelable
