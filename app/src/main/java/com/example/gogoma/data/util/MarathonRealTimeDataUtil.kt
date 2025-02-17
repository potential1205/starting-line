package com.example.gogoma.data.util

import android.content.Context
import android.util.Log
import com.example.gogoma.data.dto.MarathonRealTimeData
import com.example.gogoma.data.model.Friend
import com.example.gogoma.data.model.MarathonStartInitDataResponse
import com.example.gogoma.data.repository.UserDistanceRepository
import java.util.Timer

import kotlin.concurrent.fixedRateTimer


class MarathonRealTimeDataUtil(private val context: Context) {

    private var timer: Timer? = null

    private val startTime = System.currentTimeMillis()

    private var marathonRealTimeData = MarathonRealTimeData (
        time = 0,
        totalDistance = 0, // cm
        currentDistance = 0, // cm
        currentDistanceRate = 0.0f, // ex) 0.98
        targetPace = 0, // 초 (400페이스 -> 240초)
        currentPace = 0, // 초 (400페이스 -> 240초)
        targetTime = 0, // 초
        currentTime = 0, // 초
        state = "G", // G, Y, R
        myRank = 0,
        totalMemberCount = 0,
        friendInfoList = listOf(),
        userId = 0,
        marathonId = 0,
        userName = "",
        marathonTitle = ""
    )

     fun setReadyData(marathonReadyData: MarathonStartInitDataResponse) {
        marathonRealTimeData.totalDistance = marathonReadyData.runningDistance * 100000 // runningDistance -> km, totalDistance -> cm
        marathonRealTimeData.targetPace =
            (marathonReadyData.targetPace / 100) * 60 + (marathonReadyData.targetPace % 100)
        marathonRealTimeData.targetTime = marathonReadyData.runningDistance * marathonRealTimeData.targetPace
        marathonRealTimeData.totalMemberCount = marathonReadyData.friendList.size
        marathonRealTimeData.friendInfoList = marathonReadyData.friendList
        marathonRealTimeData.userName = marathonReadyData.userName
        marathonRealTimeData.userId = marathonReadyData.userId
        marathonRealTimeData.marathonId = marathonReadyData.marathonId
        marathonRealTimeData.marathonTitle = marathonReadyData.marathonTitle
        marathonRealTimeData.time = System.currentTimeMillis()

         Log.d("marathon", "[Marathon Ready] MarathonRealTimeData: $marathonRealTimeData")
    }

    fun getMarathonRealTimeData(): MarathonRealTimeData {
        return marathonRealTimeData
    }

    fun updateData() {
        val elapsedTimeSeconds = ((System.currentTimeMillis() - startTime) / 1000)

        marathonRealTimeData.time = System.currentTimeMillis()
        marathonRealTimeData.currentTime = elapsedTimeSeconds

        UserDistanceRepository.getUserCumulativeDistance(userId = 56) { distance ->
            if (distance != null) {
                marathonRealTimeData.currentDistance = distance
                marathonRealTimeData.currentDistanceRate =
                    if (marathonRealTimeData.totalDistance != 0)
                        distance.toFloat() / marathonRealTimeData.totalDistance
                    else 0.0f

                // 현재 페이스 계산: km 당 걸린 초
                // currentDistance는 cm 단위이므로 km = currentDistance / 100000.0
                marathonRealTimeData.currentPace =
                    if (marathonRealTimeData.currentDistance > 0)
                        (marathonRealTimeData.currentTime / (marathonRealTimeData.currentDistance.toFloat() / 100000))
                            .toInt()
                    else 0

                // 상태 업데이트: targetPace보다 빠르면 "G", targetPace+60초 이내면 "Y", 그 외 "R"
                marathonRealTimeData.state = when {
                    marathonRealTimeData.currentPace <= marathonRealTimeData.targetPace -> "G"
                    marathonRealTimeData.currentPace <= marathonRealTimeData.targetPace + 60 -> "Y"
                    else -> "R"
                }
            } else {
                Log.d("marathon", "누적 거리 값을 가져오지 못했습니다.")
            }

            // 친구 정보 업데이트 (자신의 누적 거리 업데이트 후 호출)
            updateFriendInfoList()
            Log.d("marathon", "업데이트된 MarathonRealTimeData: $marathonRealTimeData")
        }
    }

    fun endUpdating() {
        timer?.cancel()
        timer = null
    }

    private fun updateFriendInfoList() {
        val updatedFriendList = mutableListOf<Friend>()
        val friendList = marathonRealTimeData.friendInfoList

        val myFriendInfo = Friend(
            userId = marathonRealTimeData.userId,
            friendName = marathonRealTimeData.userName,
            currentDistance = marathonRealTimeData.currentDistance,
            currentDistanceRate = if (marathonRealTimeData.totalDistance != 0)
                marathonRealTimeData.currentDistance.toFloat() / marathonRealTimeData.totalDistance
            else 0.0f,
            isMe = true,
            rank = 0
        )

        if (friendList.isEmpty()) {
            marathonRealTimeData = marathonRealTimeData.copy(friendInfoList = listOf(myFriendInfo))
            return
        }

        var completedCount = 0
        val totalFriends = friendList.size

//        friendList.forEach { friend ->
//            UserDistanceRepository.getUserCumulativeDistance(userId = friend.userId) { friendDistance ->
//                val updatedFriend = friend.copy(
//                    currentDistance = friendDistance ?: friend.currentDistance,
//                    currentDistanceRate = if (marathonRealTimeData.totalDistance != 0 && friendDistance != null)
//                        friendDistance.toFloat() / marathonRealTimeData.totalDistance else friend.currentDistanceRate
//                )
//                // 동시 접근을 대비해 동기화 처리
//                synchronized(updatedFriendList) {
//                    updatedFriendList.add(updatedFriend)
//                    completedCount++
//                    if (completedCount == totalFriends) {
//                        updatedFriendList.add(myFriendInfo)
//                        val sortedList = updatedFriendList.sortedByDescending { it.currentDistance }
//
//                        // 정렬된 리스트에 순위 부여 (1부터)
//                        sortedList.forEachIndexed { index, friend ->
//                            friend.rank = index + 1
//                        }
//                        marathonRealTimeData = marathonRealTimeData.copy(friendInfoList = sortedList)
//                    }
//                }
//            }
//        }



    }

    // -------------------------------------------GPS---------------------------------------------

}

