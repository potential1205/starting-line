package com.ssafy.gogomawatch.presentation.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.SnapLayoutInfoProvider
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.Text
import androidx.wear.tooling.preview.devices.WearDevices
import com.ssafy.gogomawatch.R

@Composable
fun TeamScreen() {
    Box() {
        // ProgressBar
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                progress = 0.5f,
                modifier = Modifier.fillMaxSize(),
                strokeWidth = 10.dp,
                indicatorColor = Color.Green,
                trackColor = Color.Green.copy(alpha = 0.2f)
            )
        }
        // 내부 길 및 랭킹
        Row (
            Modifier
                .fillMaxSize(0.65f)
                .align(Alignment.Center)
        ) {
            // 길 이미지
            Box(
                Modifier
                    .fillMaxWidth(0.4f)
                    .fillMaxHeight()
                    .padding(start = 5.dp, end = 5.dp)
            ) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(color = Color.White)
                )
                Box(
                    Modifier
                        .fillMaxSize()
                        .align(Alignment.Center)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_image), // 로고 이미지 리소스
                        contentDescription = "Logo",
                        modifier = Modifier
                            .size(10.dp) // 크기를 100dp로 설정
                            .align(Alignment.Center) // 가운데 정렬
                    )
                }
            }
            // 예시 데이터 리스트
            val itemsList = listOf(
                "1tem",
                "2tem",
                "3tem",
                "4tem",
                "5tem",
                "6tem",
                "7tem",
                "8tem",
                "9tem",
                "0tem"
            )

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

            LazyColumn (
                state = lazyListState,
                modifier = Modifier
                    .fillMaxHeight(),
                flingBehavior = flingBehavior
            ){
                items(itemsList.size) { index ->
                    val isCenterItem = index == lazyListState.firstVisibleItemIndex + 1
                    val scale by animateFloatAsState(
                        targetValue = if (isCenterItem) 1.3f else 1.0f,
                        label = "itemScale"
                    )
                    Box(
                        Modifier.height(40.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = itemsList[index],
                            fontSize = (25.sp * scale),
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                        )
                    } // 리스트 아이템을 텍스트로 표시
                }
            }
        }
    }

}

@Preview(device = WearDevices.SMALL_ROUND, showSystemUi = true)
@Composable
fun TeamScreenPreview() {
    TeamScreen()
}