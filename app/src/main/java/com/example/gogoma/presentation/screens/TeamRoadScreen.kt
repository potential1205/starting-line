package com.example.gogoma.presentation.screens

import android.graphics.Paint
import android.graphics.Typeface
import android.util.Log
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.tooling.preview.devices.WearDevices
import kotlin.random.Random
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.wear.compose.material.MaterialTheme
import com.example.gogoma.R
import com.example.gogoma.presentation.data.FriendInfo
import com.example.gogoma.presentation.viewmodel.MarathonDataViewModel
import kotlin.math.absoluteValue

data class Location(val name: String, val x: Float, val y: Float, val distance: Int)

@Composable
fun TeamRoadScreen(marathonDataViewModel: MarathonDataViewModel) {
    // ViewModel에서 상태를 가져오기
    val friendInfoList = marathonDataViewModel.marathonState.collectAsState().value.friendInfoList

    val me = friendInfoList.find { it.isMe }
    if(me == null) {
        //내가 없으면 에러
        return
    }

    val myColor = MaterialTheme.colors.primary
    val otherColor = MaterialTheme.colors.secondary

    val configuration = LocalConfiguration.current
    val density = LocalDensity.current.density
    val screenWidth = configuration.screenWidthDp * density //디스플레이 가로 길이(px)
    val screenHeight = configuration.screenHeightDp * density //디스플레이 세로 길이(px)
    val roadTopWidth = screenWidth - 100 * density // 도로 상단 폭(px)
    val roadBottomWidth = screenWidth + 100 * density // 도로 하단 폭(px)

    // 레이더에 표시될 범위
    val distanceRange = marathonDataViewModel.distanceRange.collectAsState().value

    val peopleWithLocation = remember(friendInfoList) {
        friendInfoList.filter { it.userId != me.userId && it.gapDistance.absoluteValue <= distanceRange }
            .map { person ->
                // 거리 차이 계산: (내 거리 - 사람의 거리) / 100m로 화면 Y 위치를 비례적으로 계산
                val normalizedY = (-person.gapDistance.toFloat() + distanceRange) / (2 * distanceRange)
                val adjustedY = (screenHeight - 50f) * normalizedY + 25f

                // Y축 반전
//                val invertedY = screenHeight - adjustedY - 25f

                // 도로 폭 기준으로 가로 길이 계산해야 함
//                val randomX = Random.nextFloat() * roadTopWidth
                val fixedX = screenWidth * 3 / 4

                Location(person.friendName, fixedX, adjustedY, person.currentDistance)
            }
    }

    // .3s마다 반복되는 도로 중앙성 애니메이션 효과
    val infiniteTransition = rememberInfiniteTransition()
    val dashOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 40f, // dash 패턴 길이에 맞게 조절 가능
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 300, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 50.dp)
        ) {
            val roadHeight = size.height
            val centerX = size.width / 2
            val centerY = size.height / 2
            val topY = centerY - roadHeight / 2
            val bottomY = centerY + roadHeight / 2

            val roadTopLeft = Offset(centerX - roadTopWidth / 2, topY)
            val roadTopRight = Offset(centerX + roadTopWidth / 2, topY)
            val roadBottomLeft = Offset(centerX - roadBottomWidth / 2, bottomY)
            val roadBottomRight = Offset(centerX + roadBottomWidth / 2, bottomY)

            // 도로 배경(그라데이션)
            drawPath(
                path = Path().apply {
                    moveTo(roadBottomLeft.x, roadBottomLeft.y)
                    lineTo(roadBottomRight.x, roadBottomRight.y)
                    lineTo(roadTopRight.x, roadTopRight.y)
                    lineTo(roadTopLeft.x, roadTopLeft.y)
                    close()
                },
                brush = Brush.verticalGradient(
                    colors = listOf(Color.Gray, Color.Gray.copy(alpha = 0f)),
                    startY = bottomY,
                    endY = topY
                )
            )

            // 도로 테두리
            drawLine(
                color = Color.White.copy(alpha = 0.3f),
                start = roadBottomLeft,
                end = roadTopLeft,
                strokeWidth = 4f
            )
            drawLine(
                color = Color.White.copy(alpha = 0.3f),
                start = roadBottomRight,
                end = roadTopRight,
                strokeWidth = 4f
            )

            // 중앙선 (점선, 애니메이션 효과 있음)
            drawLine(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.White, Color.White.copy(alpha = .2f)),
                    startY = bottomY,
                    endY = topY
                ),
                start = Offset(centerX, bottomY),
                end = Offset(centerX, topY),
                strokeWidth = 6f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 20f), dashOffset)
            )

            //다른 사람 위치 점
            peopleWithLocation.forEach { location ->
                val center = Offset(location.x, location.y)

                drawCircle(
                    color = otherColor,
                    radius = 10f,
                    center = center
                )

//                //글자
//                val firstChar = location.name.firstOrNull().toString() ?: ""
//                // 텍스트 크기 계산
//                val paint = Paint().apply {
//                    color = android.graphics.Color.WHITE
//                    textSize = 30f
//                    typeface = Typeface.DEFAULT_BOLD
//                    textAlign = Paint.Align.CENTER
//                }
//                val textWidth = paint.measureText(firstChar)
//                val textHeight = paint.textSize
//
//
//                drawContext.canvas.nativeCanvas.drawText(
//                    firstChar,
//                    center.x,
//                    center.y + 13f,
//                    paint
//                )
            }
        }

        Image(
            painter = painterResource(id = R.drawable.icon_navigation),
            contentDescription = "Marathon Image",
            modifier = Modifier
                .size(40.dp)
                .align(Alignment.Center),
            colorFilter = ColorFilter.tint(myColor)
        )
    }
}

@Preview(device = WearDevices.SMALL_ROUND, showSystemUi = true)
@Composable
fun TeamRoadScreenPreview() {
    //Preview가 안 보이는 이유는 me가 null이라 return 당하기 때문
    TeamRoadScreen(MarathonDataViewModel())
}
