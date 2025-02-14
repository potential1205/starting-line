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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import com.ssafy.gogomawatch.presentation.components.TeamProgressBar
import com.ssafy.gogomawatch.presentation.components.TeamStatus
import com.ssafy.gogomawatch.presentation.data.FriendInfo

@Composable
fun TeamScreen() {
    val strokeWidth = 10
    val screenHeight30 = (LocalConfiguration.current.screenHeightDp - 26) / 3

    // 위치 재정비 위해 LazyListState 초기화
    val lazyListState = rememberLazyListState()


    // 내가 달린 거리 값 저장 (isMe = true인 값 찾기)
    val myCurrentDistance by remember {
        mutableStateOf(friendInfoList.find { it.isMe }?.currentDistance ?: 0)
    }

    // 중앙에 위치한 친구의 rank 추출
    val centerItemRank by remember {
        derivedStateOf {
            val centerIndex = lazyListState.firstVisibleItemIndex
            friendInfoList.getOrNull(centerIndex)?.rank ?: 1
        }
    }

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
                // 등수 부분
                val density = LocalDensity.current

                // 위치 재정비
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
                val itemsWithPadding = listOf(null) + friendInfoList + listOf(null)

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
                        if (itemsWithPadding[index] != null) {
                            TeamStatus(itemsWithPadding[index]!!, screenHeight30, scale, myCurrentDistance)
                        } else {
                            // 같은 크기의 빈 Box 삽입
                            Box(
                                Modifier
                                    .fillMaxWidth()
                                    .height(screenHeight30.dp)
                                    .padding(end = 6.45.dp)
                            )
                        }
                    }
                }
            }
        }

        // ProgressBar
        TeamProgressBar(friendInfoList,  centerItemRank)
    }
}

// 예시 데이터 리스트
val friendInfoList = listOf(
    FriendInfo(userId = 1, friendName = "김정민", currentDistance = 920000, currentDistanceRate = 0.92f, isMe = false, rank = 1),
    FriendInfo(userId = 2, friendName = "서이수", currentDistance = 897760, currentDistanceRate = 0.897f, isMe = true, rank = 2),
    FriendInfo(userId = 3, friendName = "신지호", currentDistance = 850000, currentDistanceRate = 0.85f, isMe = false, rank = 3),
    FriendInfo(userId = 4, friendName = "박지현", currentDistance = 800000, currentDistanceRate = 0.8f, isMe = false, rank = 4),
    FriendInfo(userId = 5, friendName = "이준수", currentDistance = 775500, currentDistanceRate = 0.775f, isMe = false, rank = 5),
    FriendInfo(userId = 6, friendName = "강효민", currentDistance = 750000, currentDistanceRate = 0.75f, isMe = false, rank = 6),
    FriendInfo(userId = 7, friendName = "서지수", currentDistance = 720000, currentDistanceRate = 0.72f, isMe = false, rank = 7),
    FriendInfo(userId = 8, friendName = "남궁은성", currentDistance = 700000, currentDistanceRate = 0.7f, isMe = false, rank = 8),
    FriendInfo(userId = 9, friendName = "김찬", currentDistance = 680000, currentDistanceRate = 0.68f, isMe = false, rank = 9),
    FriendInfo(userId = 10, friendName = "김찬우", currentDistance = 650000, currentDistanceRate = 0.65f, isMe = false, rank = 10),
    FriendInfo(userId = 11, friendName = "정다빈", currentDistance = 630000, currentDistanceRate = 0.63f, isMe = false, rank = 11)
)


@Preview(device = WearDevices.SMALL_ROUND, showSystemUi = true)
@Composable
fun TeamScreenPreview() {
    TeamScreen()
}