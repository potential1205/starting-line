package com.example.gogoma.presentation.data

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
    var time: Long,
    var totalDistance: Int,
    var currentDistance: Int,
    var currentDistanceRate: Float,
    var targetPace: Int,
    var currentPace: Int,
    var targetTime: Int,
    var currentTime: Int,
    var state: String,
    var myRank: Int,
    var totalMemberCount: Int,
    var friendInfoList: List<FriendInfo>,
    var marathonTitle: String
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