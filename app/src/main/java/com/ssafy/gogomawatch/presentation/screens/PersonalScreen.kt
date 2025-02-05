package com.ssafy.gogomawatch.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.compose.material.MaterialTheme
import com.ssafy.gogomawatch.presentation.components.ProgressBar
import com.ssafy.gogomawatch.presentation.viewmodel.PersonalStateViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ssafy.gogomawatch.presentation.components.ChangeColor
import com.ssafy.gogomawatch.presentation.components.PersonalStatus

@Composable
fun PersonalScreen () {
    val personalStateViewModel: PersonalStateViewModel = viewModel() // ViewModel을 주입
    // ViewModel에서 상태를 가져옵니다
    val personalState = personalStateViewModel.personalState.value

    // ChangeColor 함수로 색상 계산
    val currentColor = ChangeColor(personalState.targetPace, personalState.currentPace)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        contentAlignment = Alignment.Center
    ) {
        ProgressBar (distance = personalState.distance, totalDistance = personalState.totalDistance, currentPace = personalState.currentPace, targetPace = personalState.targetPace, currentColor)

        PersonalStatus(title = "페이스", current = formatPace(personalState.currentPace), goal = formatPace(personalState.targetPace), currentColor = currentColor, unit = "/km")
    }
}

// 임시 데이터 클래스
data class PersonalState (
    var distance: Float = 0.0f, // 현재 달린 거리
    val totalDistance: Float,
    var currentPace: Float = 0.0f, // 현재 페이스
    val targetPace: Float
)

// 초 단위를 "분:초" 형식으로 변환하는 함수
fun formatPace(seconds: Float): String {
    val minutes = (seconds / 60).toInt()
    val remainingSeconds = (seconds % 60).toInt()
    return "%d:%02d".format(minutes, remainingSeconds)
}

@Preview
@Composable
fun PersonalScreenPreview() {
    PersonalScreen()
}