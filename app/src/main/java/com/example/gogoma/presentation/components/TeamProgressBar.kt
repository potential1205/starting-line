package com.example.gogoma.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.tooling.preview.devices.WearDevices
import com.example.gogoma.presentation.theme.GogomaWatchTheme
import kotlin.math.cos
import kotlin.math.sin

// 임시 친구 정보 데이터 클래스
data class FriendInfo(
    val userId: Int,
    val friendName: String,
    val currentDistanceRate: Float,
    val isMe: Boolean
)

@Composable
fun TeamProgressBar(
    friendInfoList: List<FriendInfo> // 친구들의 진행도 리스트 (0.0 ~ 1.0)
) {
    val myProgress = friendInfoList.find { it.isMe }?.currentDistanceRate ?: 0f

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // 2. 원형 프로그레스 바 (나의 진행도 표시)
        CircularProgressIndicator(
            progress = myProgress,
            modifier = Modifier.fillMaxSize(),
            strokeWidth = 10.dp,
            indicatorColor = Color.LightGray,
            trackColor = Color.DarkGray
        )

        // 3. 친구들의 진행도를 점으로 표시
        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind {
                    val radius = size.width / 2
                    val centerX = size.width / 2
                    val centerY = size.height / 2
                    val dotRadius = 5.dp.toPx() // 점 크기

                    friendInfoList.forEach { friend ->
                        val angle =
                            360 * friend.currentDistanceRate.coerceIn(0f, 1f) - 90 // 12시 방향 기준
                        val radian = Math.toRadians(angle.toDouble())

                        val x = centerX + (radius - 5.dp.toPx()) * cos(radian)
                        val y = centerY + (radius - 5.dp.toPx()) * sin(radian)

                        drawCircle(
                            color = if (friend.isMe) Color.Green else Color.Red, // '나'는 초록색, 친구들은 빨간색
                            radius = dotRadius,
                            center = Offset(x.toFloat(), y.toFloat())
                        )
                    }
                }
        )
    }
}

@Preview(device = WearDevices.SMALL_ROUND, showSystemUi = true)
@Composable
fun TeamProgressBarPreview() {
    GogomaWatchTheme {
        TeamProgressBar(
            friendInfoList = listOf(
                FriendInfo(userId = 1, friendName = "김철수", currentDistanceRate = 0.2f, isMe = false),
                FriendInfo(userId = 2, friendName = "이영희", currentDistanceRate = 0.4f, isMe = true), // '나' (초록색)
                FriendInfo(userId = 3, friendName = "박민수", currentDistanceRate = 0.4015643f, isMe = false),
                FriendInfo(userId = 4, friendName = "최지훈", currentDistanceRate = 0.7f, isMe = false)
            )
        )
    }
}