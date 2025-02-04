package com.ssafy.gogomawatch.presentation.components

import android.os.CountDownTimer
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import kotlin.random.Random

@Composable
fun ProgressBar(distance: Int, totalDistance: Int, currentPace: Float, targetPace: Float) {
    var progress by remember { mutableStateOf(0f) } // Progress percentage

    // Calculate progress as a percentage
    val progress_dis = (distance / totalDistance.toFloat()).coerceIn(0f, 1f)

    // ChangeColor 함수로 색상 계산
    val progressBarColor = ChangeColor(targetPace, currentPace)

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Circular Progress Bar
        Box(
            modifier = Modifier
                .size(200.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                progress = progress_dis,
                modifier = Modifier.size(200.dp),
                strokeWidth = 10.dp,
                indicatorColor = progressBarColor,
                trackColor = progressBarColor.copy(alpha = 0.2f)
            )
        }
    }
}
