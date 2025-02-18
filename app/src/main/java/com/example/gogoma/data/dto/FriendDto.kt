package com.example.gogoma.data.dto

data class FriendDto(
    val userId: Int,
    val friendName: String,
    var currentDistance: Int,       // cm 단위
    var currentDistanceRate: Float,
    var isMe: Boolean,
    var rank: Int,
    var gapDistance : Int
)