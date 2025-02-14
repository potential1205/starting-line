package com.example.gogoma.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewModelScope
import com.example.gogoma.presentation.data.MarathonData
import com.example.gogoma.presentation.data.MarathonRealTimeData
import com.example.gogoma.presentation.screens.PersonalState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class MarathonDataViewModel : ViewModel() {
    private val _marathonState = mutableStateOf(
        MarathonData(
            time = System.currentTimeMillis(),
            totalDistance = 2000, // 예제: 2km
            currentDistance = 0,
            currentDistanceRate = 0f,
            targetPace = 330, // 목표 페이스 (초)
            currentPace = 330, // 현재 페이스 (초)
            targetTime = 0,
            currentTime = 0,
            state = "running",
            myRank = 1,
            totalMemberCount = 5,
            friendInfoList = emptyList()
        )
    )

    val marathonState: State<MarathonData> = _marathonState

    // 현재 인덱스 상태 추가
    private val _currentIndex = mutableStateOf(0)
    val currentIndex: State<Int> = _currentIndex

    // 시간 추적 (초 단위)
    private val _elapsedTime = mutableStateOf(0)
    val elapsedTime: State<Int> = _elapsedTime

    // 색상 상태 추가
    private val _currentColor = mutableStateOf(Color.Gray)
    val currentColor: State<Color> = _currentColor

    init {
        // 목표 시간 계산
        calculateTargetTime()

        // 5초마다 currentPace 갱신
        viewModelScope.launch {
            while (true) {
                delay(5000L)
                updateCurrentPace()
                updateColor() // 색상 업데이트
            }
        }

        // 1초마다 distance 갱신
        viewModelScope.launch {
            while (_marathonState.value.currentDistance < _marathonState.value.totalDistance) {
                delay(1000L)
                updateCurrentDistance()
            }
        }

        // 1초마다 시간 갱신
        viewModelScope.launch {
            while (true) {
                delay(1000L)
                updateElapsedTime()
            }
        }
    }

    private fun generateRandomPace(): Float {
        return (270..450).random().toFloat() // 4분 30초(270초) ~ 7분 30초(450초) 범위에서 랜덤 선택
    }

    // 인덱스 변경 함수
    fun nextStatus(total: Int) {
        _currentIndex.value = (_currentIndex.value + 1) % total
    }

    // 현재 페이스 업데이트
    private fun updateCurrentPace() {
        val newPace = generateRandomPace()
        _marathonState.value = _marathonState.value.copy(
        )
    }

    // 거리 업데이트
    private fun updateCurrentDistance() {
        val randomIncrement = (Random.nextFloat() * 0.015f + 0.005f) // 5m~20m를 km 단위로 변환
        val increment = (randomIncrement * 1000).toInt() // m 단위로 변환
        val newDistance = (_marathonState.value.currentDistance + increment)
            .coerceAtMost(_marathonState.value.totalDistance)
        val newRate = newDistance.toFloat() / _marathonState.value.totalDistance

    }

    // 경과 시간 업데이트
    private fun updateElapsedTime() {
        val newTime = _marathonState.value.currentTime + 1
        _marathonState.value = newTime
    }

    // 색상 업데이트
    private fun updateColor() {
        val currentPace = _marathonState.value.currentPace
        val targetPace = _marathonState.value.targetPace

        val paceDifference = currentPace - targetPace

        val color = when {
            paceDifference <= 0 -> Color.Green
            paceDifference <= 30 -> Color.Yellow
            else -> Color.Red
        }

        _currentColor.value = color
    }
}
