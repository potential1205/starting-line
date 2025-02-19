package com.example.gogoma.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import androidx.compose.ui.text.style.TextAlign
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
import com.example.gogoma.theme.PartialSansKR
import com.example.gogoma.ui.components.BottomBarButtonFull
import com.example.gogoma.ui.components.FormattedDate
import com.example.gogoma.ui.components.HashTag
import com.example.gogoma.ui.components.InfoTableRow
import com.example.gogoma.ui.components.MarathonDetailItem
import com.example.gogoma.ui.components.PaymentDetails
import com.example.gogoma.ui.components.TextBoxSmall
import com.example.gogoma.ui.components.TopBarArrow
import com.example.gogoma.viewmodel.MarathonDetailViewModel

@Composable
fun MarathonDetailScreen(marathonId: Int, navController: NavController){
    val marathonDetailViewModel: MarathonDetailViewModel = viewModel()
    val marathonDetail by marathonDetailViewModel.marathonDetail.collectAsState()

    val listState = rememberLazyListState()

    // 네비게이션 상태 저장 핸들
    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle

    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabTitles = listOf("대회 정보", "대회 이미지")

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

    marathonDetail?.let { detail ->
        Scaffold (
            topBar = {
                Column {
                    TopBarArrow (
                        title = if (listState.firstVisibleItemIndex >= 1) detail.marathon.title else "",
                        isDisplay = if (listState.firstVisibleItemIndex >= 1) true else false,
                        onBackClick = { navController.popBackStack() }
                    )
                    if(listState.firstVisibleItemIndex >= 2) {
                        TabRow(
                            selectedTabIndex = selectedTabIndex,
                            containerColor = MaterialTheme.colorScheme.background,
                            contentColor = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(top = 20.dp, start = 20.dp, end = 20.dp)
                        ) {
                            tabTitles.forEachIndexed { index, title ->
                                Tab(
                                    selected = selectedTabIndex == index,
                                    onClick = { selectedTabIndex = index },
                                    text = {
                                        Text(
                                            text = title,
                                            style = TextStyle(
                                                fontSize = 13.sp, // 원하는 폰트 크기로 변경
                                                fontWeight = FontWeight.Normal // 원하는 폰트 두께로 변경
                                            )
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            },
            bottomBar = {
                val isOpen = detail.marathon.marathonStatus == "OPEN"
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
                LazyColumn(state = listState) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.background)
                                .padding(start = 20.dp, top = 25.dp, end = 20.dp, bottom = 30.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
                        ) {
                            Text(
                                text = detail.dday,
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Normal,
                                )
                            )
                            Text(
                                text = detail.marathon.title,
                                style = TextStyle(
                                    fontSize = 19.sp,
                                    fontFamily = PartialSansKR,
                                    fontWeight = FontWeight.Normal,
                                )
                            )
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.fillMaxWidth().height(16.dp).background(MaterialTheme.colorScheme.tertiary))
                    }
                    // 탭 레이아웃
                    item {
                        if(listState.firstVisibleItemIndex < 2) {
                            TabRow(
                                selectedTabIndex = selectedTabIndex,
                                containerColor = MaterialTheme.colorScheme.background,
                                contentColor = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.padding(top = 20.dp, start = 20.dp, end = 20.dp)
                            ) {
                                tabTitles.forEachIndexed { index, title ->
                                    Tab(
                                        selected = selectedTabIndex == index,
                                        onClick = { selectedTabIndex = index },
                                        text = {
                                            Text(
                                                text = title,
                                                style = TextStyle(
                                                    fontSize = 13.sp, // 원하는 폰트 크기로 변경
                                                    fontWeight = FontWeight.Normal // 원하는 폰트 두께로 변경
                                                )
                                            )
                                        }
                                    )
                                }
                            }
                        }

                        // 스크롤 가능한 컨텐츠
                        Column (
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 20.dp, vertical = 20.dp)
                        ) {
                            when (selectedTabIndex) {
                                // 마라톤 상세 정보
                                0 -> MarathonDetailItem(detail)
                                // 대회 이미지
                                1 -> Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(color = MaterialTheme.colorScheme.background)
                                        .padding(top = 10.dp, bottom = 30.dp),
                                    verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.Top),
                                    horizontalAlignment = Alignment.Start,
                                ) {
                                    if(detail.marathon.infoImage.isNullOrEmpty() && detail.marathon.courseImage.isNullOrEmpty()){
                                        Text(
                                            text = "대회 이미지가 없습니다.",
                                            textAlign = TextAlign.Center,
                                            fontSize = 12.sp,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                    if(!detail.marathon.infoImage.isNullOrEmpty()){
                                        ContentTitle("대회 설명")
                                        ImageOrPlaceholder(detail.marathon.infoImage)
                                    }
                                    if(!detail.marathon.courseImage.isNullOrEmpty()){
                                        ContentTitle("대회 코스")
                                        ImageOrPlaceholder(detail.marathon.courseImage)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    } ?: run {
        // 로딩 중 처리
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            Text("Loading...")
        }
    }
}

@Composable
fun ContentTitle(text: String) {
    val fontSizeInDp = with(LocalDensity.current) { 13.sp.toDp() }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 5.dp)
    ) {
        Box(
            modifier = Modifier
                .width(5.dp)
                .height(fontSizeInDp)
                .background(MaterialTheme.colorScheme.primary)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = text,
            style = TextStyle(
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal
            )
        )
    }
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
            onDismissRequest = {
                showDialog = false
           },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                usePlatformDefaultWidth = false
            )
        ) {
            ImageViewerDialog(painter, onDismiss = { showDialog = false })
        }
    }
}

@Composable
fun ImageViewerDialog(painter: Painter, onDismiss: () -> Unit) {
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
            .clickable {
                onDismiss()
            }
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