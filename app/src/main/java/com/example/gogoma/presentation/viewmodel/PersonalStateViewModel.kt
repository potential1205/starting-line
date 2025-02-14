package com.example.gogoma.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewModelScope
import com.example.gogoma.presentation.screens.PersonalState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class PersonalStateViewModel : ViewModel() {
    private val _personalState = mutableStateOf(PersonalState(totalDistance = 2.0f, targetPace = 330f))
    val personalState: State<PersonalState> = _personalState

    // 현재 인덱스 상태 추가
    private val _currentIndex = mutableStateOf(0)
    val currentIndex: State<Int> = _currentIndex

    // 시간 추적 (초 단위)
    private val _elapsedTime = mutableStateOf(0)
    val elapsedTime: State<Int> = _elapsedTime

    // 목표 시간 상태 추가
    private val _targetTime = mutableStateOf(0)
    val targetTime: State<Int> = _targetTime

    // 색상 상태 추가
    private val _currentColor = mutableStateOf(Color.Gray)
    val currentColor: State<Color> = _currentColor

    init {
        // 목표 시간 계산
        calculateTargetTime()

        // 5초마다 currentPace를 갱신
        viewModelScope.launch {
            while (true) {
                delay(5000L)
                _personalState.value = _personalState.value.copy(currentPace = generateRandomPace())
                updateColor() // 색상 업데이트
            }
        }

        // 1초마다 distance를 갱신
        viewModelScope.launch {
            while (_personalState.value.distance < _personalState.value.totalDistance) {
                delay(1000L) // 1초 대기
                val randomIncrement = Random.nextFloat() * 0.015f + 0.005f // 5m~20m를 km 단위로 변환 (0.005km ~ 0.02km)
                val newDistance = (_personalState.value.distance + randomIncrement).coerceAtMost(_personalState.value.totalDistance)
                _personalState.value = _personalState.value.copy(distance = newDistance)
            }
        }

        // 1초마다 시간 갱신
        viewModelScope.launch {
            while (true) {
                delay(1000L) // 1초 대기
                _elapsedTime.value += 1
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

    // 목표 시간 계산 함수
    private fun calculateTargetTime() {
        val targetTimeInSeconds = (personalState.value.targetPace * personalState.value.totalDistance).toInt()
        _targetTime.value = targetTimeInSeconds
    }

    // 색상 계산 함수
    private fun updateColor() {
        val currentPace = _personalState.value.currentPace
        val targetPace = _personalState.value.targetPace

        // 페이스 차이를 계산하여 색상을 결정
        val paceDifference = currentPace - targetPace

        val color = when {
            paceDifference <= 0 -> Color.Green // 목표보다 빠를 때
            paceDifference <= 30 -> Color.Yellow // 목표보다 약간 느림 (차이 30초 이하)
            else -> Color.Red // 목표보다 너무 느림 (차이 30초 초과)
        }

        _currentColor.value = color
    }
}
