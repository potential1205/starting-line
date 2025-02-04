package com.ssafy.gogomawatch.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.ssafy.gogomawatch.presentation.components.ProgressBar
import com.ssafy.gogomawatch.presentation.viewmodel.PersonalStateViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun PersonalScreen () {
    val personalStateViewModel: PersonalStateViewModel = viewModel() // ViewModel을 주입
    // ViewModel에서 상태를 가져옵니다
    val personalState = personalStateViewModel.personalState.value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        contentAlignment = Alignment.Center
    ) {
        ProgressBar (distance = personalState.distance, totalDistance = personalState.totalDistance, currentPace = personalState.currentPace, targetPace = personalState.targetPace)

        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "My Pace: ${formatPace(personalState.currentPace)}",
                color = MaterialTheme.colors.onBackground,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp) // 텍스트에만 패딩 추가
            )
        }
    }
}

// 임시 데이터 클래스
data class PersonalState (
    var distance: Int = 0, // 현재 달린 거리
    val totalDistance: Int,
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