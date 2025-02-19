package com.example.gogoma.presentation.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.gestures.snapping.SnapLayoutInfoProvider
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.wear.tooling.preview.devices.WearDevices
import com.example.gogoma.presentation.components.TeamProgressBar
import com.example.gogoma.presentation.components.TeamStatus
import com.example.gogoma.presentation.viewmodel.MarathonDataViewModel
import kotlin.math.absoluteValue

@Composable
fun TeamScreen(marathonDataViewModel: MarathonDataViewModel) {

    // ViewModel에서 상태를 가져오기
    val friendInfoList = marathonDataViewModel.marathonState.collectAsState().value.friendInfoList

    val strokeWidth = 10

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

    // 특정 거리(현재: 100m) 내에 친구가 있는지 확인
    val shouldNavigateToRoadScreen by remember {
        derivedStateOf {
            friendInfoList.any { it.gapDistance.absoluteValue <= 10000 }
        }
    }

    Box() {

        // 등수
        Box(
            modifier = Modifier
                .padding(strokeWidth.dp + 3.dp)
                .align(Alignment.Center)
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
                val itemsWithPadding = listOf(null) + friendInfoList + listOf(null)

                LazyColumn (
                    state = lazyListState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height((25*2+25*1.9).dp),
                    flingBehavior = flingBehavior,
                    verticalArrangement = Arrangement.Center
                ){
                    items(itemsWithPadding.size) { index ->
                        val isCenterItem = index == lazyListState.firstVisibleItemIndex + 1
                        val scale by animateFloatAsState(
                            targetValue = if (isCenterItem) 1.9f else 1f,
                            label = "itemScale"
                        )

                        // 색상 결정 (isMe → 초록색, 중앙 아이템(단, isMe 아님) → 노란색, 기본 → 흰색)
                        val color = when {
                            itemsWithPadding[index]?.isMe == true -> Color(0xFF2680FF)
                            isCenterItem -> Color(0xFFFFFFFF)
                            else -> Color(0xFFB9B9B9)
                        }

                        if (itemsWithPadding[index] != null) {
                            TeamStatus(itemsWithPadding[index]!!, scale, myCurrentDistance, color)
                        } else {
                            // 같은 크기의 빈 Box 삽입
                            Box(
                                Modifier
                                    .fillMaxWidth()
                                    .height(25.dp)
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

@Preview(device = WearDevices.SMALL_ROUND, showSystemUi = true)
@Composable
fun TeamScreenPreview() {
//    TeamScreen()
}