package com.ssafy.gogomawatch.presentation.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.SnapLayoutInfoProvider
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.Text
import androidx.wear.tooling.preview.devices.WearDevices
import com.ssafy.gogomawatch.R
import com.ssafy.gogomawatch.presentation.data.FriendInfo

@Composable
fun TeamScreen() {
    val strokeWidth = 10
    val screenHeight30 = (LocalConfiguration.current.screenHeightDp - 26) / 3
    Box() {
        Box(
            modifier = Modifier
                .padding(strokeWidth.dp + 3.dp)
        ) {
            Row (
                Modifier
                    .fillMaxSize()
                    .clip(CircleShape),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // 길 이미지
                Box(
                    Modifier
                        .fillMaxWidth(.4f)
                        .fillMaxHeight()
                        .padding(start = 8.45.dp, end = 4.65.dp)
                ) {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(color = Color(0xFF3D3D3D))
                            .align(Alignment.Center),
                        contentAlignment = Alignment.Center
                    ) {
                        //원
                        Canvas (
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            val circleRadius = 4.dp.toPx()
                            val centerX = size.width / 2
                            val height20 = size.height / 4

                            drawCircle(
                                color = Color.White,
                                radius = circleRadius,
                                center = Offset(centerX, height20)
                            )

                            drawCircle(
                                color = Color.White,
                                radius = circleRadius,
                                center = Offset(centerX, height20 * 3)
                            )
                        }
                        //나의 위치가 가장 위에 옴
                        Image(
                            painter = painterResource(id = R.drawable.icon_navigation), // 로고 이미지 리소스
                            contentDescription = "my location image",
                            colorFilter = ColorFilter.tint(Color(0xFF62DA74)),
                            modifier = Modifier
                                .size(24.dp)
                                .align(Alignment.Center)
                        )
                    }
                }
                // 예시 데이터 리스트
                val friendInfoList = listOf(
                    FriendInfo(userId = 1, friendName = "김정민", currentDistance = 920000, currentDistanceRate = 0.92, isMe = false),
                    FriendInfo(userId = 2, friendName = "서이수", currentDistance = 897760, currentDistanceRate = 0.897, isMe = true),
                    FriendInfo(userId = 3, friendName = "신지호", currentDistance = 850000, currentDistanceRate = 0.85, isMe = false),
                    FriendInfo(userId = 4, friendName = "박지현", currentDistance = 800000, currentDistanceRate = 0.8, isMe = false),
                    FriendInfo(userId = 5, friendName = "이준수", currentDistance = 775500, currentDistanceRate = 0.775, isMe = false),
                    FriendInfo(userId = 6, friendName = "강효민", currentDistance = 750000, currentDistanceRate = 0.75, isMe = false),
                    FriendInfo(userId = 7, friendName = "서지수", currentDistance = 720000, currentDistanceRate = 0.72, isMe = false),
                    FriendInfo(userId = 8, friendName = "남궁은성", currentDistance = 700000, currentDistanceRate = 0.7, isMe = false),
                    FriendInfo(userId = 9, friendName = "김찬", currentDistance = 680000, currentDistanceRate = 0.68, isMe = false),
                    FriendInfo(userId = 10, friendName = "김찬우", currentDistance = 650000, currentDistanceRate = 0.65, isMe = false),
                    FriendInfo(userId = 11, friendName = "정다빈", currentDistance = 630000, currentDistanceRate = 0.63, isMe = false)
                )

                //이름 리스트
                val friendRankNameList = friendInfoList.mapIndexed { index, friend ->
                    "${index + 1}등 ${friend.friendName}"
                }

                // 등수 부분
                // 위치 재정비
                val lazyListState = rememberLazyListState()

                val density = LocalDensity.current

                val snappingLayout = remember(lazyListState, density) {
                    val snapPosition = object : SnapPosition {
                        override fun position(
                            layoutSize: Int,
                            itemSize: Int,
                            beforeContentPadding: Int,
                            afterContentPadding: Int,
                            itemIndex: Int,
                            itemCount: Int
                        ): Int {
                            // 보이는 아이템의 index와 offset
                            if (lazyListState.layoutInfo.visibleItemsInfo.size > 1) {
                                val firstIndex = lazyListState.layoutInfo.visibleItemsInfo[0].index
                                val firstOffset = lazyListState.layoutInfo.visibleItemsInfo[0].offset
                                val secondIndex = lazyListState.layoutInfo.visibleItemsInfo[1].index
                                val secondOffset = lazyListState.layoutInfo.visibleItemsInfo[1].offset
                            }

                            return 0 // item의 움직일 포지션값
                        }
                    }
                    SnapLayoutInfoProvider(lazyListState, snapPosition)
                }

                val flingBehavior = rememberSnapFlingBehavior(snappingLayout)

                // 스크롤 중이냐 아니냐 check. 멈추면 보이고 있는 첫번째 index로 animateScroll
                LaunchedEffect(key1 = lazyListState.isScrollInProgress) {
                    if (!lazyListState.isScrollInProgress) {
                        lazyListState.animateScrollToItem(lazyListState.firstVisibleItemIndex)
                    }
                }

                // 앞 뒤로 빈 아이템 추가
                val emptyItem = ""
                val itemsWithPadding = listOf(emptyItem) + friendRankNameList + listOf(emptyItem) + listOf(emptyItem)

                LazyColumn (
                    state = lazyListState,
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(end = 5.dp),
                    flingBehavior = flingBehavior
                ){
                    items(itemsWithPadding.size) { index ->
                        val isCenterItem = index == lazyListState.firstVisibleItemIndex + 1
                        val scale by animateFloatAsState(
                            targetValue = if (isCenterItem) 1f else .8f,
                            label = "itemScale"
                        )
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .height(screenHeight30.dp)
                                .padding(end = 6.45.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = itemsWithPadding[index],
                                fontSize = (20 * scale).sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .scale(scale)
                            )
                        } // 리스트 아이템을 텍스트로 표시
                    }
                }
            }
        }

        // ProgressBar
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                progress = 0.5f,
                modifier = Modifier.fillMaxSize(),
                strokeWidth = strokeWidth.dp,
                indicatorColor = Color.Green,
                trackColor = Color.Green.copy(alpha = 0.2f)
            )
        }
    }
}

@Preview(device = WearDevices.SMALL_ROUND, showSystemUi = true)
@Composable
fun TeamScreenPreview() {
    TeamScreen()
}