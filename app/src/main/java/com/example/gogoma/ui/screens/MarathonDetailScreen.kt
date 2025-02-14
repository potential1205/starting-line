package com.example.gogoma.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.gogoma.R
import com.example.gogoma.theme.BrandColor1
import com.example.gogoma.ui.components.BottomBarButtonFull
import com.example.gogoma.ui.components.FormattedDate
import com.example.gogoma.ui.components.HashTag
import com.example.gogoma.ui.components.InfoTableRow
import com.example.gogoma.ui.components.TextBoxSmall
import com.example.gogoma.ui.components.TopBarArrow
import com.example.gogoma.viewmodel.MarathonDetailViewModel

@Composable
fun MarathonDetailScreen(marathonId: Int, navController: NavController){
    val marathonDetailViewModel: MarathonDetailViewModel = viewModel()
    val marathonDetail by marathonDetailViewModel.marathonDetail.collectAsState()

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
                val isOpen = marathonDetail!!.marathon.marathonStatus == "OPEN"
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
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)){
                // 마라톤 상세 정보
                LazyColumn {
                    item {
                        ImageOrPlaceholder(marathonDetail!!.marathon.thumbnailImage, true)
                    }
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(color = MaterialTheme.colorScheme.background)
                                .padding(start = 20.dp, top = 10.dp, end = 20.dp, bottom = 10.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.Top),
                            horizontalAlignment = Alignment.Start,
                        ) {
                            //접수중
                            Row (
                                modifier = Modifier
                                    .padding(top = 20.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),
                                verticalAlignment = Alignment.Top,
                            ) {
                                val marathonStatus = marathonDetail!!.marathon.marathonStatus
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
                                    .padding(top = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = marathonDetail!!.marathon.title,
                                    style = TextStyle(
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .padding(bottom = 20.dp),
                                horizontalArrangement = Arrangement.spacedBy(6.0371174812316895.dp, Alignment.Start),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                 marathonDetail!!.marathonTypeList.forEach { type ->
                                    HashTag(type.courseType)
                                }
                            }

                            HorizontalDivider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 12.dp),
                                thickness = 1.dp,
                                color = Color(0xFFDDDDDD)
                            )

                            //대회 정보
                            val formattedRaceStartTime = FormattedDate(marathonDetail!!.marathon.raceStartTime)
                            InfoTableRow(label = "대회일시", value = formattedRaceStartTime)
                            InfoTableRow(label = "대회장소", value = marathonDetail!!.marathon.location)
                            InfoTableRow(label = "대회종목", value = marathonDetail!!.marathonTypeList.joinToString("/") { "${it.courseType}" })
                            InfoTableRow(
                                label = "접수기간",
                                value = if (marathonDetail!!.marathon.registrationStartDateTime == null && marathonDetail!!.marathon.registrationEndDateTime == null) {
                                    "접수 선착순"
                                } else {
                                    "접수 ${marathonDetail!!.marathon.registrationStartDateTime ?: ""}~${marathonDetail!!.marathon.registrationEndDateTime ?: ""}"
                                },
                            )
                            // marathonTypeList 그룹화 및 매니아 처리
                            val groupedCourseTypes = marathonDetail!!.marathonTypeList
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
                            InfoTableRow(label = "대회주최", value = marathonDetail!!.marathon.hostList.joinToString(", ") { "${it}" })
                            InfoTableRow(label = "대회주관", value = marathonDetail!!.marathon.organizerList.joinToString(", ") { "${it}" })
                            InfoTableRow(label = "대회후원", value = marathonDetail!!.marathon.sponsorList.joinToString(", ") { "${it}" })
                            HorizontalDivider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 12.dp, bottom = 12.dp),
                                thickness = 1.dp,
                                color = Color(0xFFDDDDDD)
                            )
                        }
                    }

                    //대회 이미지
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(color = MaterialTheme.colorScheme.background)
                                .padding(start = 20.dp, top = 10.dp, end = 20.dp, bottom = 30.dp),
                            verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.Top),
                            horizontalAlignment = Alignment.Start,
                        ) {
                            if(!marathonDetail!!.marathon.infoImage.isNullOrEmpty()){
                                ContentTitle("대회 설명")
                                ImageOrPlaceholder(marathonDetail!!.marathon.infoImage)
                            }
                            if(!marathonDetail!!.marathon.courseImage.isNullOrEmpty()){
                                ContentTitle("대회 코스")
                                ImageOrPlaceholder(marathonDetail!!.marathon.courseImage)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ContentTitle(text: String){
    Text(
        text = text,
        style = TextStyle(
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        ),
        modifier = Modifier
            .padding(top = 5.dp),
    )
}

@Composable
fun ImageOrPlaceholder(imageUrl: String?, isThumb: Boolean = false) {
    var showDialog by remember { mutableStateOf(false) }
    val painter =
        rememberAsyncImagePainter(ImageRequest.Builder
            (LocalContext.current).data(data = imageUrl).apply(block = fun ImageRequest.Builder.() {
            crossfade(true) // 이미지 로딩 시 부드러운 전환 효과
            placeholder(R.drawable.icon_running) // 로딩 중에 보여줄 이미지
            error(R.drawable.icon_close) // 에러 발생 시 보여줄 이미지
            size(Size.ORIGINAL) // 원본 크기로 이미지를 로드
        }).build()
        )

    //그냥 이미지
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(if (isThumb) 200.dp else Dp.Unspecified)
            .then(
                if (!isThumb) Modifier.clickable { showDialog = true } else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        if (imageUrl.isNullOrEmpty()) {
            Text("no image")
        } else {
            Image(
                painter = painter,
                contentDescription = "Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }

    //확대 보기 이미지
    if(showDialog && !isThumb) {
        Dialog(
            onDismissRequest = {showDialog = false},
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            )
        ) {
            ImageViewerDialog(painter)
        }
    }
}

@Composable
fun ImageViewerDialog(painter: Painter) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    val screenWidth = with(density) {configuration.screenWidthDp.dp.toPx()}
    val screenHeight = with(density) {configuration.screenHeightDp.dp.toPx()}

    val imageWidth = painter.intrinsicSize.width
    val imageHeight = painter.intrinsicSize.height

    var scale by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painter,
            contentDescription = "Image can zoom",
            modifier = Modifier
                .graphicsLayer(
                    scaleX = scale, //수평 확대/축소
                    scaleY = scale, //수직 확대/축소
                    translationX = offsetX, //수평 이동
                    translationY = offsetY //수직 이동
                )
                .align(Alignment.Center)
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        //확대/축소 비율을 1배에서 3배로 제한함
                        scale = (scale * zoom).coerceIn(1f, 3f)

                        // 확대된 이미지 크기
                        val scaledImageWidth = imageWidth * scale
                        val scaledImageHeight = imageHeight * scale

                        // 이동 가능한 최대 거리 (화면보다 이미지가 클 경우에만 이동 가능)
                        val maxOffsetX = ((scaledImageWidth - screenWidth) / 2).coerceAtLeast(0f)
                        val maxOffsetY = ((scaledImageHeight - screenHeight) / 2).coerceAtLeast(0f)

                        // 이동 거리 조절 (화면 밖으로 나가지 않도록 제한)
                        offsetX = (offsetX + pan.x * scale).coerceIn(-maxOffsetX, maxOffsetX)
                        offsetY = (offsetY + pan.y * scale).coerceIn(-maxOffsetY, maxOffsetY)
                    }
                }
        )
    }
}



@Preview
@Composable
fun MarathonDetailScreenPreview(){
    MarathonDetailScreen(25, rememberNavController())
}