package com.example.gogoma.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.gogoma.R
import com.example.gogoma.theme.BrandColor1
import com.example.gogoma.ui.components.BottomBarButtonFull
import com.example.gogoma.ui.components.FormattedDate
import com.example.gogoma.ui.components.InfoTableRow
import com.example.gogoma.ui.components.TextBoxSmall
import com.example.gogoma.ui.components.TopBarArrow
import com.example.gogoma.viewmodel.MarathonDetailViewModel

@Composable
fun MarathonDetailScreen(marathonId: Int, navController: NavController){
    val marathonDetailViewModel: MarathonDetailViewModel = viewModel()
    val marathonDetail = marathonDetailViewModel.marathonDetail

    // 네비게이션 상태 저장 핸들
    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle

    // 상세 정보 로드
    LaunchedEffect (marathonId) {
        marathonDetailViewModel.loadMarathonDetail(marathonId)
    }

    // 데이터가 로드되면 savedStateHandle에 저장
    LaunchedEffect(marathonDetail) {
        marathonDetail?.let {
            println("상세 페이지에서 데이터 저장: $it")
            savedStateHandle?.set("marathonDetail_$marathonId", it)
        }
    }

    // 로딩 중 처리
    if (marathonDetail == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            Text("Loading...")
        }
    } else {
        Scaffold (
            topBar = { TopBarArrow (
                    title = "대회",
                    onBackClick = { navController.popBackStack() }
                )
            },
            bottomBar = {
                val isOpen = marathonDetail.marathon.marathonStatus == "OPEN"
                BottomBarButtonFull(
                    text = "신청하기",
                    backgroundColor = if (isOpen) BrandColor1 else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                    textColor = if (isOpen) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    onClick = {
                        if (isOpen) {
                            navController.navigate("payment/$marathonId")
                        }
                    },
                    enabled = isOpen
                )
            }
        ){ paddingValues ->
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues)){
                // 마라톤 상세 정보
                LazyColumn {
                    item {
                        ImageOrPlaceholder(marathonDetail.marathon.thumbnailImage)
                    }
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(color = MaterialTheme.colorScheme.background)
                                .padding(start = 13.dp, top = 10.dp, end = 13.dp, bottom = 10.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.Top),
                            horizontalAlignment = Alignment.Start,
                        ) {
                            //접수중
                            Row (
                                modifier = Modifier
                                    .padding(start = 8.dp, top = 20.dp, end = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),
                                verticalAlignment = Alignment.Top,
                            ) {
                                val marathonStatus = marathonDetail.marathon.marathonStatus
                                val marathonStatusText = when (marathonStatus) {
                                    "OPEN" -> "접수중"
                                    "CLOSED" -> "접수 종료"
                                    "FINISHED" -> "접수 마감"
                                    else -> marathonStatus
                                }

                                val marathonStatusBackgroundColor = when (marathonStatus) {
                                    "OPEN" -> MaterialTheme.colorScheme.secondary
                                    "CLOSED" -> MaterialTheme.colorScheme.error
                                    "FINISHED" -> Color.Gray
                                    else -> MaterialTheme.colorScheme.secondary
                                }

                                TextBoxSmall(
                                    text = marathonStatusText,
                                    backgroundColor = marathonStatusBackgroundColor
                                )
                                TextBoxSmall(
                                    text = "D-1",
                                    backgroundColor = marathonStatusBackgroundColor
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .padding(start = 10.dp, top = 8.dp, end = 10.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = marathonDetail.marathon.title,
                                    style = TextStyle(
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight(800)
                                    )
                                )
                            }

                        }
                    }
                    item {
                        Column() {
                            val formattedRaceStartTime = FormattedDate(marathonDetail.marathon.raceStartTime)
                            InfoTableRow(label = "대회일시", value = formattedRaceStartTime)
                            InfoTableRow(label = "대회장소", value = marathonDetail.marathon.location)
                            InfoTableRow(label = "대회종목", value = marathonDetail.marathonTypeList.joinToString("/") { "${it.courseType}" })
                            InfoTableRow(
                                label = "접수기간",
                                value = if (marathonDetail.marathon.registrationStartDateTime == null && marathonDetail.marathon.registrationEndDateTime == null) {
                                    "접수 선착순"
                                } else {
                                    "접수 ${marathonDetail.marathon.registrationStartDateTime ?: ""}~${marathonDetail.marathon.registrationEndDateTime ?: ""}"
                                },
                            )
                            // marathonTypeList 그룹화 및 매니아 처리
                            val groupedCourseTypes = marathonDetail.marathonTypeList
                                .groupBy { it.courseType } // courseType 기준으로 그룹화
                                .mapValues { (_, types) ->
                                    // 각 courseType에 대해 "매니아"가 있는지 확인
                                    val regularPrice = types.firstOrNull { it.etc.isEmpty() }?.price ?: ""
                                    val maniaPrices = types.filter { it.etc == "매니아" }.joinToString(" ") { it.price }

                                    // "매니아" 가격과 일반 가격을 합쳐서 출력
                                    if (maniaPrices.isNotEmpty()) {
                                        "${regularPrice}원 (매니아 : ${maniaPrices}원)"
                                    } else {
                                        "${regularPrice}원"
                                    }
                                }
                            InfoTableRow(label = "대회가격", value = groupedCourseTypes.entries.joinToString("\n") {
                                "${it.key}: ${it.value}"
                            })
                            InfoTableRow(label = "대회주최", value = marathonDetail.marathon.hostList.joinToString(", ") { "${it}" })
                            InfoTableRow(label = "대회주관", value = marathonDetail.marathon.organizerList.joinToString(", ") { "${it}" })
                            InfoTableRow(label = "대회후원", value = marathonDetail.marathon.sponsorList.joinToString(", ") { "${it}" })
                        }
                    }


                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.background)
                                .padding(start = 16.dp, end = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(0.dp, Alignment.CenterHorizontally),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            ImageOrPlaceholder(marathonDetail.marathon.infoImage)
                            ImageOrPlaceholder(marathonDetail.marathon.courseImage)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ImageOrPlaceholder(imageUrl: String?) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(MaterialTheme.colorScheme.tertiary),
        contentAlignment = Alignment.Center
    ) {
        if (imageUrl.isNullOrEmpty()) {
            Text("no image")
        } else {
        val painter =
            rememberAsyncImagePainter(ImageRequest.Builder
                (LocalContext.current).data(data = imageUrl).apply(block = fun ImageRequest.Builder.() {
                crossfade(true) // 이미지 로딩 시 부드러운 전환 효과
                placeholder(R.drawable.icon_running) // 로딩 중에 보여줄 이미지
                error(R.drawable.icon_close) // 에러 발생 시 보여줄 이미지
            }).build()
            )
        Image(
            painter = painter,
            contentDescription = "Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
    }
}