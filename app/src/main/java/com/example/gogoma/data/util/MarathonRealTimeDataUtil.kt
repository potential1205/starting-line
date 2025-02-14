package com.example.gogoma.data.util

import com.example.gogoma.data.dto.MarathonReadyDto
import com.example.gogoma.data.dto.MarathonRealTimeData
import com.example.gogoma.data.repository.UserDistanceRepository
import java.util.Timer
import kotlin.concurrent.fixedRateTimer

class MarathonRealTimeDataUtil (private val marathonReadyData: MarathonReadyDto) {

    private var timer: Timer? = null

    init {
        setInitData()
    }

    private var marathonRealTimeData = MarathonRealTimeData(
        time = System.currentTimeMillis(),
        totalDistance = 0,
        currentDistance = 0,
        currentDistanceRate = 0.00f,
        targetPace = 0,
        currentPace = 0,
        targetTime = 0,
        currentTime = 0,
        state = "G",
        myRank = 0,
        totalMemberCount = 0,
        friendInfoList = listOf()
    )

    private fun setInitData() {
        marathonRealTimeData.time = System.currentTimeMillis()
        marathonRealTimeData.totalDistance = 0
        marathonRealTimeData.targetPace = marathonReadyData.targetPace
        marathonRealTimeData.targetTime =  calTargetTime(marathonReadyData.targetPace, marathonReadyData.runningDistance)
        marathonRealTimeData.totalMemberCount = marathonReadyData.friendList.size
        marathonRealTimeData.friendInfoList = marathonReadyData.friendList
    }

    private fun updateData() {
        marathonRealTimeData = marathonRealTimeData.copy(
            time = System.currentTimeMillis(),
            currentTime = marathonRealTimeData.currentTime + 1,


        )

        UserDistanceRepository.getUserCumulativeDistance(userId = 5) { distance ->
            if (distance != null) {
                println("누적 거리: $distance")
                marathonRealTimeData.totalDistance = distance
            } else {
                println("누적 거리 값을 가져오지 못했습니다.")
            }
        }


    }

    fun startUpdating() {
        startUpdatingData()
    }

    private fun startUpdatingData() {
        timer = fixedRateTimer(name = "MarathonDataUpdater", daemon = true, initialDelay = 0L, period = 500L) {
            updateData()
        }
    }

    fun endUpdating() {
        timer?.cancel()
        timer = null
    }


    private fun calTargetTime(targetTime: Int, targetPace: Int): Int {
        return targetTime * targetPace
    }

    fun getMarathonRealTimeData(): MarathonRealTimeData {
        return marathonRealTimeData
    }
}