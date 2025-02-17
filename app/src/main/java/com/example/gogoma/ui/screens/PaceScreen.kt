package com.example.gogoma.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.gogoma.GlobalApplication
import com.example.gogoma.R
import com.example.gogoma.ui.components.BottomBar
import com.example.gogoma.ui.components.ButtonBasic
import com.example.gogoma.ui.components.TopBarArrow
import com.example.gogoma.utils.TokenManager
import com.example.gogoma.viewmodel.BottomSheetViewModel
import com.example.gogoma.viewmodel.MarathonViewModel
import com.example.gogoma.viewmodel.PaceViewModel
import com.example.gogoma.viewmodel.UserViewModel

@Composable
fun PaceScreen(
    navController: NavController,
    userViewModel: UserViewModel,
    bottomSheetViewModel: BottomSheetViewModel,
    marathonViewModel: MarathonViewModel = viewModel(),
    paceViewModel: PaceViewModel,
) {
    val context = LocalContext.current

    val marathon by paceViewModel.upcomingMarathonInfoResponse.collectAsState()
    val marathonStartInitDataResponse by paceViewModel.marathonStartInitDataResponse.collectAsState()
    val friendList by paceViewModel.friendList.collectAsState()

    var isColumn by remember { mutableStateOf(false) }
    var totalTextWidth by remember { mutableStateOf(0) }
    var totalColumnWidth by remember { mutableStateOf(0) }

    val screenWidth = LocalConfiguration.current.screenWidthDp
    val contentWidth = screenWidth - 64  // 패딩값

    val textModifier = Modifier.onGloballyPositioned { coordinates ->
        totalTextWidth = coordinates.size.width // 해당 요소의 가로 길이를 측정
    }
    val columnModifier = Modifier.onGloballyPositioned { coordinates ->
        totalColumnWidth = coordinates.size.width
    }

    // 뒤로 가기 동작 정의
    BackHandler(enabled = bottomSheetViewModel.isBottomSheetVisible) {
        // 모달창이 열려 있을 때 뒤로 가기 버튼 처리
        if (bottomSheetViewModel.isSubPageVisible) {
            // 모달 내에서 페이지가 바뀌었으면 이전 페이지로 돌아가게 처리
            bottomSheetViewModel.goBackToPreviousPage()
        } else {
            // 처음 연 모달 창이라면 모달 닫기
            bottomSheetViewModel.hideBottomSheet()
        }
    }

    LaunchedEffect(Unit) {
        paceViewModel.getUpcomingMarathonInfo(
            TokenManager.getAccessToken(context = context).toString())
    }

    LaunchedEffect(marathon) {
        if (marathon != null) {
            paceViewModel.getInitData(
                TokenManager.getAccessToken(context = context).toString(),
                marathon!!.marathon.id
            )
        }
    }
    LaunchedEffect(totalTextWidth + totalColumnWidth) {
        isColumn = (totalTextWidth + totalColumnWidth) > contentWidth
    }

    Scaffold(
        topBar = {
            TopBarArrow(
                title = "달려보기",
                onBackClick = { navController.popBackStack() }
            )
        },
        bottomBar = { BottomBar(navController = navController, userViewModel) }
    ) { paddingValues ->
        // marathon이 null인 경우
        if (marathon == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "아직 준비된 대회가 없습니다",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                )
            }

        } else {
            // marathon이 null이 아닐 때 기존 화면 구성
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(start = 32.dp, top = 76.dp, end = 32.dp, bottom = 76.dp),
                    verticalArrangement = Arrangement.spacedBy(40.dp, Alignment.Top),
                    horizontalAlignment = Alignment.Start,
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color(0xFFF8F8F8))
                            .padding(start = 30.dp, top = 46.dp, end = 30.dp, bottom = 42.dp),
                    ) {
                        if (isColumn) { // 반응형 레이아웃: 세로 배치
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.SpaceBetween,
                                horizontalAlignment = Alignment.Start,
                            ) {
                                Text(
                                    text = marathon!!.marathon.title,
                                    style = TextStyle(
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF000000)
                                    )
                                )
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
                                    horizontalAlignment = Alignment.Start,
                                ) {
                                    Text(
                                        text = "목표 페이스",
                                        style = TextStyle(
                                            fontSize = 12.sp,
                                            color = Color(0xFF000000)
                                        )
                                    )
                                    Text(
                                        text = if (marathonStartInitDataResponse?.targetPace == 0) {
                                            "페이스를 설정해주세요"
                                        } else {
                                            marathonStartInitDataResponse?.targetPace?.toString() ?: "페이스를 설정해주세요"
                                        },
                                        style = TextStyle(
                                            fontSize = 35.sp,
                                            fontWeight = FontWeight(400),
                                            color = Color(0xFF000000)
                                        )
                                    )


                                    ButtonBasic(
                                        iconResId = R.drawable.icon_settings,
                                        text = "페이스 설정",
                                        contentColor = Color(0xFF8A8A8A),
                                        round = 0.dp,
                                        onClick = { bottomSheetViewModel.showBottomSheet() }
                                    )
                                }
                            }
                        } else { // 반응형 레이아웃: 가로 배치
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top,
                            ) {
                                Text(
                                    text = marathon!!.marathon.title,
                                    style = TextStyle(
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF000000)
                                    ),
                                    modifier = textModifier
                                )
                                Column(
                                    modifier = columnModifier,
                                    verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
                                    horizontalAlignment = Alignment.End,
                                ) {
                                    Text(
                                        text = "목표 페이스",
                                        style = TextStyle(
                                            fontSize = 12.sp,
                                            color = Color(0xFF000000)
                                        )
                                    )
                                    Text(
                                        text = if (marathonStartInitDataResponse?.targetPace == 0) {
                                            "페이스를 설정해주세요"
                                        } else {
                                            marathonStartInitDataResponse?.targetPace?.toString() ?: "페이스를 설정해주세요"
                                        },
                                        style = TextStyle(
                                            fontSize = 35.sp,
                                            fontWeight = FontWeight(400),
                                            color = Color(0xFF000000)
                                        )
                                    )


                                    ButtonBasic(
                                        iconResId = R.drawable.icon_settings,
                                        text = "페이스 설정",
                                        contentColor = Color(0xFF8A8A8A),
                                        round = 0.dp,
                                        onClick = { bottomSheetViewModel.showBottomSheet() }
                                    )
                                }
                            }
                        }
                    }

                    Column(
                        verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.Bottom),
                        horizontalAlignment = Alignment.Start,
                    ) {
                        Row(
                            modifier = Modifier.padding(start = 6.dp, end = 6.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            val participantText = if (friendList.isNotEmpty()) {
                                "${friendList[0].name} 님 외 ${friendList.size - 1}명 참가"
                            } else {
                                "참가자 정보 없음"
                            }

                            Text(
                                text = participantText,
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    color = Color(0xFF000000)
                                )
                            )
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.Start),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            if (friendList.isNotEmpty()) {
                                friendList.take(4).forEach { friend ->
                                    AsyncImage( // Coil 라이브러리를 이용한 비동기 이미지 로드
                                        model = friend.profileImage, // 이미지가 없을 경우 기본 이미지 사용
                                        contentDescription = "${friend.name}의 프로필 이미지",
                                        modifier = Modifier.size(30.dp),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }
                        }
                    }
                }
                ButtonBasic(
                    text = "준비",
                    modifier = Modifier.fillMaxWidth(),
                    round = 0.dp,
                    onClick = {
                        marathonViewModel.marathonReady()

                        marathonStartInitDataResponse?.let {
                            paceViewModel.saveMarathonDataToDB(
                                it
                            )
                        }

                        navController.navigate("watchConnect")
                    }
                )
            }
        }
    }
}


@Preview(showBackground = true, widthDp = 320)
@Composable
fun PaceScreenSmallPreview(){
    PaceScreen(navController = rememberNavController(), userViewModel = UserViewModel(), bottomSheetViewModel = BottomSheetViewModel(), paceViewModel = PaceViewModel(GlobalApplication()))
}

@Preview(showBackground = true)
@Composable
fun PaceScreenPreview(){
    PaceScreen(navController = rememberNavController(), userViewModel = UserViewModel(), bottomSheetViewModel = BottomSheetViewModel(), paceViewModel = PaceViewModel(GlobalApplication()))
}