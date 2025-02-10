package com.example.gogoma.ui.screens

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.gogoma.R
import com.example.gogoma.data.model.Marathon
import com.example.gogoma.ui.components.BottomBar
import com.example.gogoma.ui.components.ButtonBasic
import com.example.gogoma.ui.components.TopBarArrow
import com.example.gogoma.viewmodel.UserViewModel

@Composable
fun PaceScreen(
    navController: NavController,
    userViewModel: UserViewModel,
    onPaceSettingClick: () -> Unit,
) {

    val marathon = Marathon(
        id = 1,
        title = "서울 마라톤 2025",
        registrationStartDateTime = "2025-01-01T00:00:00",
        registrationEndDateTime = "2025-01-30T23:59:59",
        raceStartTime = "2025-02-10T07:00:00",
        accountBank = "국민은행",
        accountNumber = "123-456-7890-123",
        accountName = "서울 마라톤",
        location = "서울시 강남구",
        city = "서울",
        year = "2025",
        month = "05",
        region = "서울",
        district = "강남구",
        hostList = listOf("서울시", "대한체육회"),
        organizerList = listOf("서울 마라톤 조직위원회"),
        sponsorList = listOf("삼성전자", "롯데", "국민은행"),
        qualifications = "18세 이상 남녀 누구나 참여 가능",
        marathonStatus = "접수 중",
        viewCount = 1250,
        thumbnailImage = "https://example.com/thumbnail.jpg",
        infoImage = "https://example.com/info.jpg",
        courseImage = "https://example.com/course.jpg",
        formType = 1,  // 예시로 1로 설정
        formUrl = "https://example.com/form"
    )

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

    LaunchedEffect (totalTextWidth + totalColumnWidth) {
        isColumn = (totalTextWidth + totalColumnWidth) > contentWidth
    }

    Scaffold (
        topBar = { TopBarArrow (
            title = "달려보기",
            onBackClick = { navController.popBackStack() }
        )
        },
        bottomBar = { BottomBar(navController = navController, userViewModel) }
    ){ paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)){
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
                    if(isColumn){//반응형
                        // 대회 박스
                        Column (
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.SpaceBetween,
                            horizontalAlignment = Alignment.Start,
                        ) {
                            Text(
                                text = marathon.title,
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF000000),
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
                                        color = Color(0xFF000000),
                                    )
                                )
                                Text(
                                    text = "240",
                                    style = TextStyle(
                                        fontSize = 35.sp,
                                        fontWeight = FontWeight(400),
                                        color = Color(0xFF000000),
                                    )
                                )
                                ButtonBasic(
                                    iconResId = R.drawable.icon_settings,
                                    text = "페이스 설정",
                                    contentColor = Color(0xFF8A8A8A),
                                    round = 0.dp,
                                    onClick = { onPaceSettingClick() }
                                )
                            }
                        }
                    } else {
                        // 대회 박스
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top,
                        ) {
                            Text(
                                text = marathon.title,
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF000000),
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
                                        color = Color(0xFF000000),
                                    )
                                )
                                Text(
                                    text = "240",
                                    style = TextStyle(
                                        fontSize = 35.sp,
                                        fontWeight = FontWeight(400),
                                        color = Color(0xFF000000),
                                    )
                                )
                                ButtonBasic(
                                    iconResId = R.drawable.icon_settings,
                                    text = "페이스 설정",
                                    contentColor = Color(0xFF8A8A8A),
                                    round = 0.dp,
                                    onClick = { onPaceSettingClick() }
                                )
                            }
                        }
                    }
                }

                // 참여자
                Column(
                    verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.Bottom),
                    horizontalAlignment = Alignment.Start,
                ) {
                    // 참가 인원 수
                    Row(
                        modifier = Modifier
                            .padding(start = 6.dp, end = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "김용현 님 외 3명 참가",
                            style = TextStyle(
                                fontSize = 12.sp,
                                color = Color(0xFF000000),
                            )
                        )
                    }
                    // 참가자 프로필들
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.Start),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logo_image),
                            contentDescription = "profile image",
                            contentScale = ContentScale.None
                        )
                    }
                }
            }
            ButtonBasic(
                text = "날짜",
                modifier = Modifier.fillMaxWidth(),
                round = 0.dp,
                onClick = {}
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 320)
@Composable
fun PaceScreenSmallPreview(){
    PaceScreen(navController = rememberNavController(), userViewModel = UserViewModel(), onPaceSettingClick = {})
}

@Preview(showBackground = true)
@Composable
fun PaceScreenPreview(){
    PaceScreen(navController = rememberNavController(), userViewModel = UserViewModel(), onPaceSettingClick = {})
}