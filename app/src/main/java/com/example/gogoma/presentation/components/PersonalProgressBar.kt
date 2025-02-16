package com.example.gogoma.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.CircularProgressIndicator

@Composable
fun ProgressBar(currentDistanceRate: Float, progressBarColor: Color) {

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // 원형 프로그레스바
        Box(
            modifier = Modifier
                .size(200.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                progress = currentDistanceRate,
                modifier = Modifier.size(200.dp),
                strokeWidth = 10.dp,
                indicatorColor = progressBarColor,
                trackColor = progressBarColor.copy(alpha = 0.2f)
            )
        }
    }
}
