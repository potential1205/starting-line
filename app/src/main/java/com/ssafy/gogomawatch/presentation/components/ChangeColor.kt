package com.ssafy.gogomawatch.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text
import kotlinx.coroutines.delay

@Composable
fun ChangeColor(targetPace: Float, currentPace: Float): Color {
    return when {
        currentPace <= targetPace -> Color.Green  // 목표보다 빠름
        currentPace <= targetPace + 30 -> Color.Yellow  // 목표보다 약간 느림. 페이스 차이 30 이하
        else -> Color.Red  // 너무 느림. 페이스 차이 30 초과
    }
}

//// 초 단위를 "분:초" 형식으로 변환하는 함수
//fun formatPace(seconds: Float): String {
//    val minutes = (seconds / 60).toInt()
//    val remainingSeconds = (seconds % 60).toInt()
//    return "%d:%02d".format(minutes, remainingSeconds)
//}
