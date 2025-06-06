package com.example.gogoma.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.example.gogoma.presentation.data.FriendInfo
import kotlin.math.floor

@Composable
fun TeamStatus( friendInfo: FriendInfo, scale: Float, myCurrentDistance: Int, color: Color ) {
    Box(
        Modifier
            .fillMaxWidth()
            .height((25 * scale).dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${friendInfo.rank}",
                style = TextStyle(
                    fontFamily = MaterialTheme.typography.caption1.fontFamily,
                    fontSize = (10 * scale).sp,
                    fontWeight = FontWeight.Bold,
                    color = color,
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            )

            Spacer(Modifier.width(5.dp))

            Text(
                text = friendInfo.friendName,
                fontSize = (15 * scale).sp,
                textAlign = TextAlign.Center,
                color = color,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            )


            Spacer(Modifier.width(5.dp))

            Text(
                text = formatDistanceDiff(friendInfo.gapDistance) + "km",
                fontSize = (8 * scale).sp,
                textAlign = TextAlign.Center,
                color = color,
                modifier = Modifier
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