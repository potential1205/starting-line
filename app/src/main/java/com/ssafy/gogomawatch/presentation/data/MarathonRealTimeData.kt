package com.ssafy.gogomawatch.presentation.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MarathonRealTimeData(
    val path: String,
    val data: MarathonData,
    val priority: String
) : Parcelable

@Parcelize
data class MarathonData(
    val time: Long,
    val totalDistance: Int,
    val currentDistance: Int,
    val currentDistanceRate: Float,
    val targetPace: Int,
    val currentPace: Int,
    val targetTime: Int,
    val currentTime: Int,
    val state: String,
    val myRank: Int,
    val totalMemberCount: Int,
    val friendInfoList: List<FriendInfo>
) : Parcelable

@Parcelize
data class FriendInfo(
    val userId: Int,
    val friendName: String,
    val currentDistance: Int,
    val currentDistanceRate: Float,
    val isMe: Boolean,
    val rank: Int
) : Parcelable