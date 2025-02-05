package com.ssafy.gogomawatch.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import com.ssafy.gogomawatch.presentation.screens.PersonalState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class PersonalStateViewModel : ViewModel() {
    private val _personalState = mutableStateOf(PersonalState(totalDistance = 2000.0f, targetPace = 330f))
    val personalState: State<PersonalState> = _personalState

    init {
        // 5초마다 currentPace를 갱신
        viewModelScope.launch {
            while (true) {
                delay(2000L)
                _personalState.value = _personalState.value.copy(currentPace = generateRandomPace())
            }
        }

        // 1초마다 distance를 갱신
        viewModelScope.launch {
            while (_personalState.value.distance < _personalState.value.totalDistance) {
                delay(1000L) // 1초 대기
                val randomIncrement = Random.nextInt(5, 21) // 5~20m 랜덤 증가
                val newDistance = (_personalState.value.distance + randomIncrement).coerceAtMost(_personalState.value.totalDistance)
                _personalState.value = _personalState.value.copy(distance = newDistance)
            }
        }
    }

    private fun generateRandomPace(): Float {
        return (270..450).random().toFloat() // 4분 30초(270초) ~ 7분 30초(450초) 범위에서 랜덤 선택
    }
}
