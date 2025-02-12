package com.example.gogoma.data.dto

data class MarathonReadyDto(
    var userId: Int = 0,
    var marathonId: Int = 0,
    var userName: String = "",
    var targetPace: Int = 0,
    var runningDistance: Int = 0,
    var marathonTitle: String = "",
    var friendList: List<FriendDto> = emptyList()
)