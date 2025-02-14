package com.ssafy.gogomawatch.presentation.components

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
import com.ssafy.gogomawatch.presentation.data.FriendInfo
import com.ssafy.gogomawatch.presentation.theme.GogomaWatchTheme
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun TeamProgressBar(
    friendInfoList: List<FriendInfo>,
    rank: Int
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
                    val dotRadius = 4.dp.toPx() // 점 크기

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

                    // '나'인 경우 초록색 점을 다시 위에 그림
                    friendInfoList.find { it.isMe }?.let { friend ->
                        val angle = 360 * friend.currentDistanceRate.coerceIn(0f, 1f) - 90
                        val radian = Math.toRadians(angle.toDouble())

                        val x = centerX + (radius - 5.dp.toPx()) * cos(radian)
                        val y = centerY + (radius - 5.dp.toPx()) * sin(radian)

                        drawCircle(
                            color = Color.Green,
                            radius = dotRadius,
                            center = Offset(x.toFloat(), y.toFloat())
                        )
                    }

                    // '랭크 대상'인 경우 노란색 점을 다시 위에 그림
                    friendInfoList.find { it.rank == rank && !it.isMe }?.let { friend ->
                        val angle = 360 * friend.currentDistanceRate.coerceIn(0f, 1f) - 90
                        val radian = Math.toRadians(angle.toDouble())

                        val x = centerX + (radius - 5.dp.toPx()) * cos(radian)
                        val y = centerY + (radius - 5.dp.toPx()) * sin(radian)

                        drawCircle(
                            color = Color.Yellow,
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
                FriendInfo(userId = 1, friendName = "김철수", currentDistance = 5000, currentDistanceRate = 0.5f, isMe = false, rank = 1),
                FriendInfo(userId = 2, friendName = "이영희", currentDistance = 4050, currentDistanceRate = 0.405f, isMe = true, rank = 2),
                FriendInfo(userId = 3, friendName = "박민수", currentDistance = 4000, currentDistanceRate = 0.4f, isMe = false, rank = 3),
                FriendInfo(userId = 4, friendName = "최지훈", currentDistance = 4000, currentDistanceRate = 0.4f, isMe = false, rank = 4)
            ), rank = 3
        )
    }
}