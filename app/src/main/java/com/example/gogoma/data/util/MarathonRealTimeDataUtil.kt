package com.example.gogoma.data.util

import android.util.Log
import com.example.gogoma.data.dto.FriendDto
import com.example.gogoma.data.dto.MarathonReadyDto
import com.example.gogoma.data.dto.MarathonRealTimeData
import com.example.gogoma.data.repository.UserDistanceRepository
import java.util.Timer
import kotlin.concurrent.fixedRateTimer

class MarathonRealTimeDataUtil(private val marathonReadyData: MarathonReadyDto) {

    private var timer: Timer? = null
    private val TAG = "MarathonDataUtil"

    // 마라톤 시작 시각 (초기화 시점에 기록)
    private val startTime = System.currentTimeMillis()

    private var marathonRealTimeData = MarathonRealTimeData(
        time = startTime,
        totalDistance = 0,
        currentDistance = 0,
        currentDistanceRate = 0.0f,
        targetPace = 0,
        currentPace = 0,
        targetTime = 0,
        currentTime = 0,
        state = "G",
        myRank = 0,
        totalMemberCount = 0,
        friendInfoList = listOf(),
        userId = 0,
        marathonId = 0,
        userName = "",
        marathonTitle = ""
    )

    init {
        setInitData()
    }

    /**
     * 초기 데이터를 marathonReadyData를 기반으로 설정.
     * - runningDistance: km 단위로 가정 → cm로 변환 (1km = 100,000cm)
     * - targetPace: MMSS 형식(예, 630 -> 6분 30초)을 초 단위로 환산 (6*60 + 30 = 390초)
     * - targetTime: 총 거리(킬로미터) * targetPace(초/킬로미터)
     */
    private fun setInitData() {
        // runningDistance가 km 단위라고 가정
        marathonRealTimeData.totalDistance = marathonReadyData.runningDistance * 100000
        marathonRealTimeData.targetPace =
            (marathonReadyData.targetPace / 100) * 60 + (marathonReadyData.targetPace % 100)
        // targetTime: 총 km * 초/킬로미터
        marathonRealTimeData.targetTime = marathonReadyData.runningDistance * marathonRealTimeData.targetPace
        marathonRealTimeData.totalMemberCount = marathonReadyData.friendList.size
        marathonRealTimeData.friendInfoList = marathonReadyData.friendList
        marathonRealTimeData.userName = marathonReadyData.userName
        marathonRealTimeData.userId = marathonReadyData.userId
        marathonRealTimeData.marathonId = marathonReadyData.marathonId
        marathonRealTimeData.marathonTitle = marathonReadyData.marathonTitle
        marathonRealTimeData.time = System.currentTimeMillis()
    }

    /**
     * 주기적으로 호출되어 마라톤 실시간 데이터를 업데이트함.
     * - 경과 시간은 시작 시각과 현재 시각의 차이를 초 단위로 계산
     * - 자신의 누적 거리와 페이스, 상태 등을 업데이트 후, 친구 정보도 업데이트함.
     */
    private fun updateData() {
        // 경과 시간을 초 단위로 계산
        val elapsedTimeSeconds = ((System.currentTimeMillis() - startTime) / 1000)
        // 현재 시간(타임스탬프) 업데이트 및 경과 시간 반영
        marathonRealTimeData.time = System.currentTimeMillis()
        marathonRealTimeData.currentTime = elapsedTimeSeconds

        // 자신의 누적 거리 업데이트 (비동기)
        UserDistanceRepository.getUserCumulativeDistance(userId = marathonRealTimeData.userId) { distance ->
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
                Log.d(TAG, "누적 거리 값을 가져오지 못했습니다.")
            }

            // 친구 정보 업데이트 (자신의 누적 거리 업데이트 후 호출)
            updateFriendInfoList()
            Log.d(TAG, "업데이트된 MarathonRealTimeData: $marathonRealTimeData")
        }
    }

    fun startUpdating() {
        startUpdatingData()
    }

    private fun startUpdatingData() {
        // 주기를 필요에 따라 조정 (예: 1초 주기로 업데이트)
        timer = fixedRateTimer(
            name = "MarathonDataUpdater",
            daemon = true,
            initialDelay = 0L,
            period = 1000L
        ) {
            updateData()
        }
    }

    fun endUpdating() {
        timer?.cancel()
        timer = null
    }

    fun getMarathonRealTimeData(): MarathonRealTimeData {
        return marathonRealTimeData
    }

    /**
     * 친구 리스트를 업데이트함.
     * 각 친구의 누적 거리(및 비율)를 비동기 호출로 가져오고, 자신의 정보도 포함한 후
     * currentDistance 기준 내림차순 정렬하고 rank를 부여함.
     */
    private fun updateFriendInfoList() {
        val updatedFriendList = mutableListOf<FriendDto>()
        val friendList = marathonRealTimeData.friendInfoList

        // 내 정보를 FriendDto로 생성
        val myFriendInfo = FriendDto(
            userId = marathonRealTimeData.userId,
            friendName = marathonRealTimeData.userName,
            currentDistance = marathonRealTimeData.currentDistance,
            currentDistanceRate = if (marathonRealTimeData.totalDistance != 0)
                marathonRealTimeData.currentDistance.toFloat() / marathonRealTimeData.totalDistance
            else 0.0f,
            isMe = true,
            rank = 0
        )

        // 만약 친구 리스트가 비어있다면, 내 정보만 설정
        if (friendList.isEmpty()) {
            marathonRealTimeData = marathonRealTimeData.copy(friendInfoList = listOf(myFriendInfo))
            return
        }

        var completedCount = 0
        val totalFriends = friendList.size

        friendList.forEach { friend ->
            UserDistanceRepository.getUserCumulativeDistance(userId = friend.userId) { friendDistance ->
                val updatedFriend = friend.copy(
                    currentDistance = friendDistance ?: friend.currentDistance,
                    currentDistanceRate = if (marathonRealTimeData.totalDistance != 0 && friendDistance != null)
                        friendDistance.toFloat() / marathonRealTimeData.totalDistance else friend.currentDistanceRate
                )
                // 동시 접근을 대비해 동기화 처리
                synchronized(updatedFriendList) {
                    updatedFriendList.add(updatedFriend)
                    completedCount++
                    if (completedCount == totalFriends) {
                        // 모든 친구 업데이트가 완료되면 내 정보도 추가
                        updatedFriendList.add(myFriendInfo)
                        // currentDistance 기준 내림차순 정렬
                        val sortedList = updatedFriendList.sortedByDescending { it.currentDistance }
                        // 정렬된 리스트에 순위 부여 (1부터)
                        sortedList.forEachIndexed { index, friend ->
                            friend.rank = index + 1
                        }
                        marathonRealTimeData = marathonRealTimeData.copy(friendInfoList = sortedList)
                    }
                }
            }
        }
    }
}
