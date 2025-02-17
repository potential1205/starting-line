package com.example.gogoma.data.dto

import com.example.gogoma.data.roomdb.entity.Friend


data class MarathonRealTimeData (
    var totalDistance: Int,         // 전체 거리 (cm 단위)
    var currentDistance: Int,       // 현재 거리 (cm 단위)
    var currentDistanceRate: Float,
    var targetPace: Int,            // 목표 페이스 (초)
    var currentPace: Int,           // 현재 페이스 (초)
    var targetTime: Int,            // 목표 시간 (초)
    var currentTime: Int,           // 현재 시간 (초)
    var state: String,              // 상태: 목표페이스 이상 "G", 1분이하 미달 "Y", 그외 "R"
    var myRank: Int,                // 내 순위
    var totalMemberCount: Int,      // 전체 멤버 수
    var friendInfoList: List<Friend>,
    var userId: Int = 0,
    var marathonId: Int = 0,
    var userName: String = "",
    var marathonTitle: String = "",
)