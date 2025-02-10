package com.ssafy.gogomawatch.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.wear.tooling.preview.devices.WearDevices
import com.ssafy.gogomawatch.presentation.components.PersonalStatus
import kotlin.math.floor

@Composable
fun PersonalScreen () {
    val personalStateViewModel: PersonalStateViewModel = viewModel() // ViewModel 주입

    // ViewModel에서 상태를 가져오기
    val personalState = personalStateViewModel.personalState.value

    // ChangeColor 함수로 색상 계산
    val currentColor = personalStateViewModel.currentColor.value

    // ViewModel에서 currentIndex 가져오기
    val currentIndex = personalStateViewModel.currentIndex.value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .clickable { personalStateViewModel.nextStatus(3) }, // 상태가 3개이므로 3 전달
        contentAlignment = Alignment.Center
    ) {
        // 프로그레스바
        ProgressBar (distance = personalState.distance, totalDistance = personalState.totalDistance, progressBarColor = currentColor)

        // 텍스트 노출 부분: 터치 시 변경
        when (currentIndex) {
            0 -> PersonalStatus(title = "페이스", current = formatPace(personalState.currentPace), goal = formatPace(personalState.targetPace), currentColor = currentColor, unit = "/km")
            1 -> PersonalStatus("이동 거리", formatDistance(personalState.distance), personalState.totalDistance.toString(), currentColor, "km")
            2 -> PersonalStatus("달린 시간", formatTime(personalStateViewModel.elapsedTime.value), formatTime(personalStateViewModel.targetTime.value), currentColor)
        }
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

// 시간 초 단위 => 시간:분:초 형식으로 변환
fun formatTime(seconds: Int): String {
    val hours = seconds / 3600 // 시간 계산 (3600초 = 1시간)
    val minutes = (seconds % 3600) / 60 // 남은 초에서 분 계산
    val remainingSeconds = seconds % 60 // 나머지 초 계산
    return "%02d:%02d:%02d".format(hours, minutes, remainingSeconds)
}

// 거리 포맷팅 함수
fun formatDistance(distance: Float): String {
    val truncatedValue = floor(distance * 100) / 100 // 소수점 둘째 자리까지 내림
    return "%.2f".format(truncatedValue)
}

@Preview(device = WearDevices.SMALL_ROUND, showSystemUi = true)
@Composable
fun PersonalScreenPreview() {
    PersonalScreen()
}