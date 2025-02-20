package com.example.gogoma.ui.screens

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
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
import kotlinx.coroutines.launch

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
        marathonViewModel.resetMarathonStopState() //중단 체크용 변수 초기화
    }

    LaunchedEffect(marathon) {
        if (marathon != null) {
            paceViewModel.getInitData(
                TokenManager.getAccessToken(context = context).toString(),
                marathon!!.marathon.id
            )
        }
    }

    LaunchedEffect(marathon) {
        if(marathon != null){
            paceViewModel.getUpcomingMarathonFriendList(
                TokenManager.getAccessToken(context = context).toString()
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
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 10.dp, top = 58.dp, end = 10.dp, bottom = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(182.dp, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(23.dp, Alignment.Top),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = "이런!",
                            style = TextStyle(
                                fontSize = 49.sp,
                                fontFamily = FontFamily(Font(R.font.partialsanskr_regular)),
                                fontWeight = FontWeight(400),
                                color = Color(0xFF000000),
                                textAlign = TextAlign.Center,
                            )
                        )
                        Text(
                            text = "아직 가까운 대회가 없습니다.",
                            style = TextStyle(
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Light,
                                color = Color.Black,
                                textAlign = TextAlign.Center,
                            )
                        )
                    }
                    Button(
                        onClick = {
                            navController.navigate("main")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2680FF)),
                        shape = RoundedCornerShape(4.dp),
                        contentPadding = PaddingValues(horizontal = 55.dp, vertical = 14.dp)
                    ) {
                        Text(
                            text = "다른 대회 둘러보기",
                            style = TextStyle(
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                }
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
                                            "${(marathonStartInitDataResponse?.targetPace ?: 0) / 100}:${((marathonStartInitDataResponse?.targetPace ?: 0) % 100).let { if(it < 10) "0$it" else it }}"
                                        },
                                        style = TextStyle(
                                            fontSize = 35.sp,
                                            fontWeight = FontWeight(400),
                                            color = Color(0xFF000000)
                                        )
                                    )


                                    Button(
                                        onClick = { bottomSheetViewModel.showBottomSheet() },
                                        modifier = Modifier
                                            .background(Color.Transparent)
                                            .border(1.dp, Color(0xFFD0D0D0), shape = RoundedCornerShape(0.dp)),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color.Transparent,
                                            contentColor = Color(0xFFD0D0D0)
                                        ),
                                        shape = RoundedCornerShape(0.dp),
                                        elevation = ButtonDefaults.elevatedButtonElevation(0.dp),
                                        contentPadding = PaddingValues(12.dp) // 내부 패딩을 4.dp로 설정
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.icon_settings),
                                            contentDescription = null,
                                            tint = Color(0xFFD0D0D0)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            "페이스 설정",
                                            color = Color(0xFFD0D0D0)
                                        )
                                    }




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
                                            "${(marathonStartInitDataResponse?.targetPace ?: 0) / 100}\"${((marathonStartInitDataResponse?.targetPace ?: 0) % 100).let { if(it < 10) "0$it" else it }}"
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
                        verticalArrangement = Arrangement.spacedBy(15.dp, Alignment.Bottom),
                        horizontalAlignment = Alignment.Start,
                    ) {
                        Row(
                            modifier = Modifier.padding(start = 6.dp, end = 6.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            val participantText = if (friendList.isNotEmpty()) {
                                "함께 뛰는 친구 ${friendList.size}명"
                            } else {
                                "참가자 정보 없음"
                            }
                            Text(
                                text = participantText,
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF000000)
                                )
                            )
                        }

                        Row(
                            modifier = Modifier.padding(start = 6.dp, end = 6.dp), // 여기서 시작 패딩 추가
                            horizontalArrangement = Arrangement.spacedBy(14.dp, Alignment.Start),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            if (friendList.isNotEmpty()) {
                                friendList.forEach { friend ->
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        val painter = if (friend.profileImage.isNullOrEmpty()) {
                                            painterResource(id = R.drawable.logo_image)
                                        } else {
                                            val secureProfileImage = friend.profileImage.replaceFirst("http://", "https://")
                                            rememberAsyncImagePainter(
                                                ImageRequest.Builder(LocalContext.current)
                                                    .data(data = secureProfileImage)
                                                    .apply {
                                                        crossfade(true)
                                                        placeholder(R.drawable.icon_running)
                                                        error(R.drawable.icon_close)
                                                    }
                                                    .build()
                                            )
                                        }
                                        Image(
                                            painter = painter,
                                            contentDescription = "Profile image of ${friend.name}",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .size(45.dp)
                                                .clip(CircleShape)
                                        )
                                        Text(
                                            text = friend.name,
                                            style = TextStyle(
                                                fontSize = 12.sp,
                                                color = Color(0xFF000000)
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }

                }
                val scope = rememberCoroutineScope()
                if(marathonStartInitDataResponse != null){
                if(marathonStartInitDataResponse!!.targetPace == 0){
                    ButtonBasic(
                        text = "준비",
                        modifier = Modifier.fillMaxWidth(),
                        round = 0.dp,
                        backgroundColor = Color(0xFF909090),
                        onClick = {
                        }
                    )
                }else {
                    ButtonBasic(
                        text = "준비",
                        modifier = Modifier.fillMaxWidth(),
                        round = 0.dp,
                        onClick = {
                            scope.launch {
                                marathonStartInitDataResponse?.let { response ->
                                    paceViewModel.saveMarathonDataToDB(response) {
                                        marathonViewModel.marathonReady()
                                    }
                                    navController.navigate("watchConnect")
                                }
                            }
                        }
                    )
                }
                    }
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