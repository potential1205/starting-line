package com.ssafy.gogomawatch.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text
import com.ssafy.gogomawatch.presentation.data.FriendInfo
import kotlin.math.floor

@Composable
fun TeamStatus( friendInfo: FriendInfo, screenHeight30: Int, scale: Float, myCurrentDistance: Int, color: Color ) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(screenHeight30.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Text(
                text = "${friendInfo.rank}등",
                fontSize = (15 * scale).sp,
                textAlign = TextAlign.Center,
                color = color,
                modifier = Modifier
                    .scale(scale)
                    .align(Alignment.CenterVertically)
            )

            Text(
                text = friendInfo.friendName,
                fontSize = (25 * scale).sp,
                textAlign = TextAlign.Center,
                color = color,
                modifier = Modifier
                    .scale(scale)
                    .align(Alignment.CenterVertically)
            )

            Text(
                text = formatDistanceDiff(friendInfo.currentDistance - myCurrentDistance) + "km",
                fontSize = (15 * scale).sp,
                textAlign = TextAlign.Center,
                color = color,
                modifier = Modifier
                    .scale(scale)
                    .align(Alignment.CenterVertically)
            )
        }
    }
}

// 거리 포맷팅 함수
fun formatDistanceDiff(distanceInCm: Int): String {
    val distanceInKm = distanceInCm / 100000f // cm -> km 변환
    val truncatedValue = floor(distanceInKm * 100) / 100 // 소수점 둘째 자리까지 내림
    return "%+.2f".format(truncatedValue)
}