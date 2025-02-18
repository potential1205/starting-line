package com.example.gogoma.data.dto

import com.example.gogoma.data.roomdb.entity.Friend


data class MarathonRealTimeData (
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
    var friendInfoList: List<FriendDto>,
    var friendList: List<Friend>,
    var userId: Int = 0,
    var marathonId: Int = 0,
    var userName: String = "",
    var marathonTitle: String = "",
)