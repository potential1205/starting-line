package com.example.gogoma.ui.screens

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.gogoma.R
import com.example.gogoma.data.dto.UserMarathonSearchDto
import com.example.gogoma.theme.BrandColor1
import com.example.gogoma.theme.BrandColor2
import com.example.gogoma.ui.components.*
import com.example.gogoma.utils.TokenManager
import com.example.gogoma.viewmodel.RegistViewModel
import com.example.gogoma.viewmodel.UserViewModel
import com.google.gson.Gson

@Composable
fun PaymentStatusScreen(
    navController: NavController,
    isSuccess: Boolean,
    registJson: String? = null,
    userViewModel: UserViewModel,
    registViewModel: RegistViewModel,
) {
    val context = LocalContext.current

    val gson = remember { Gson() }
    val regist: UserMarathonSearchDto? = registJson?.let {
        try {
            val parsed = gson.fromJson(it, UserMarathonSearchDto::class.java)
            parsed.apply {
                Log.d("PaymentStatusScreen", "✅ JSON 변환 성공: $this")
            }
        } catch (e: Exception) {
            Log.e("PaymentStatusScreen", "❌ JSON 변환 실패: ${e.message}")
            null
        }
    }

    LaunchedEffect(isSuccess) {
        if (isSuccess && regist != null) {
//            registViewModel.addRegist(regist) // 결제 성공 시 리스트에 추가 //추가로 처리하지 않아도 Screen 단계에서 load됨
            registViewModel.notifyFriends(TokenManager.getAccessToken(context = context).toString(), marathonId = regist.userMarathonId!!)
        }
    }

    Scaffold(
        topBar = { TopBarArrow(onBackClick = {navController.navigate("main")}) },
        bottomBar = { BottomBar(navController = navController, userViewModel) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            //주 내용
            ContentLayout(
                isSuccess = isSuccess,
                navController = navController,
                registInfo = regist,
            )
            //말풍선
            if(isSuccess){
                BubbleWithArrow()
            }
        }
    }
}

@Composable
fun BoxScope.BubbleWithArrow() { //Box Scope align을 쓰기 위해서 정의
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val primaryColor = MaterialTheme.colorScheme.primary

    Box(
        modifier = Modifier
            .align(Alignment.BottomEnd)
            .offset(x = -screenWidth * 0.041f, y = -4.dp)
    ) {
        Column(horizontalAlignment = Alignment.End) {
            // 말풍선 본체
            Box(
                modifier = Modifier
                    .background(
                        color = primaryColor,
                        shape = RoundedCornerShape(6.dp)
                    )
                    .padding(8.dp)
            ) {
                Text(
                    text = "대회가 가까워지면 워치를 연동해 보세요",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 13.sp
                )
            }
            // 말풍선의 삼각형 부분
            Canvas(
                modifier = Modifier
                    .width(16.dp)
                    .height(12.5.dp)
                    .offset(x = (-20).dp, y = (-1).dp)
            ) {
                val trianglePath = Path().apply {
                    moveTo(0f, 0f)
                    lineTo(size.width, 0f)
                    lineTo(size.width / 2, size.height)
                    close()
                }
                drawPath(
                    path = trianglePath,
                    color = primaryColor
                )
            }
        }
    }
}

@Composable
fun ContentLayout (
    isSuccess: Boolean,
    navController: NavController,
    registInfo: UserMarathonSearchDto? = null
) {
    val painter = if(isSuccess){
        painterResource(id = R.drawable.icon_flag_check)
    }else{
        painterResource(id = R.drawable.icon_cancel)
    }
    val pointColor = if(isSuccess){
        MaterialTheme.colorScheme.primary
    }else{
        MaterialTheme.colorScheme.secondary
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 40.dp, top = 120.dp, end = 40.dp, bottom = 110.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(25.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            painter = painter,
            contentDescription = "payment status icon",
            tint = pointColor,
            modifier = Modifier
                .size(110.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = if(isSuccess) "결제 완료" else "결제 실패",
                style = TextStyle(
                    fontSize = 25.5.sp,
                    lineHeight = 45.9.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = pointColor,
                    textAlign = TextAlign.Center,
                )
            )
            Text(
                text = if(isSuccess) "무사히 첫 출발을\n해냈습니다" else "결제에 실패했습니다.\n\n괜찮아요. 다시 출발하러 가 볼까요?",
                style = TextStyle(
                    fontSize = 13.5.sp,
                    lineHeight = 21.6.sp,
                    fontWeight = FontWeight.Light,
                    color = Color(0xFF1C1C1C),
                    textAlign = TextAlign.Center,
                )
            )
            if(!isSuccess){
                Icon(
                    painter = painterResource(id = R.drawable.icon_cheer),
                    contentDescription = "payment status icon",
                    tint = MaterialTheme.colorScheme.onBackground ,
                    modifier = Modifier
                        .size(22.dp)
                )
            }
        }
        registInfo?.let {
            RegistListItem(
                UserMarathonSearchDto(
                    paymentDateTime = it.paymentDateTime,
                    marathonTitle = it.marathonTitle,
                    raceStartDateTime = it.raceStartDateTime,
                    marathonType = it.marathonType,
                    userMarathonId = it.userMarathonId,
                    dday = it.dday
                ),
                onClick = { },
//                onClick = {navController.navigate("registDetail/${it.userMarathonId}")},
                background = Color.Transparent
            )
            Log.d("SuccessContent", registInfo.toString())
        } ?: run {
            Log.e("SuccessContent", "❌ registInfo가 null입니다.")
        }
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if(isSuccess){
                Button(
                    onClick = { navController.navigate("registList") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(size = 4.dp)
                        ),
                    contentPadding = PaddingValues(start = 18.dp, top = 10.dp, end = 18.dp, bottom = 10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_list),
                        contentDescription = "list icon",
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "신청 내역으로",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight(800),
                            color = Color(0xFFFEFEFE),
                        )
                    )
                }
            } else {
                Button(
                    onClick = {
                        navController.navigate("main")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color(0xFF909090),
                            shape = RoundedCornerShape(size = 4.dp)
                        ),
                    contentPadding = PaddingValues(start = 18.dp, top = 10.dp, end = 18.dp, bottom = 10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF909090),
                        contentColor = Color(0xFFD0D0D0)
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_home),
                        contentDescription = "list icon",
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "홈으로",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold,
                        )
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(size = 4.dp)
                        ),
                    contentPadding = PaddingValues(start = 18.dp, top = 10.dp, end = 18.dp, bottom = 10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_footprint),
                        contentDescription = "list icon",
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "결제 페이지로",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold,
                        )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ContentLayoutPreview () {
    ContentLayout(
        isSuccess = true,
        navController = rememberNavController(),
        registInfo = UserMarathonSearchDto(
            userMarathonId = 1,
            marathonTitle = "서울 마라톤 2025",
            marathonType = 1,
            dday = "D-7",
            raceStartDateTime = "2025-03-15T08:00:00",
            paymentDateTime = "2025-01-10T14:30:00"
        )
    )
}

@Preview(showBackground = true)
@Composable
fun ContentLayoutPreview2 () {
    ContentLayout(isSuccess = false, navController = rememberNavController())
}

@Preview(showBackground = true)
@Composable
fun PaymentStatusScreenPreview(){
    PaymentStatusScreen(isSuccess = true, registViewModel = RegistViewModel(), userViewModel = UserViewModel(), navController = rememberNavController())
}

@Preview(showBackground = true)
@Composable
fun PaymentStatusScreenPreview2(){
    PaymentStatusScreen(isSuccess = false, registViewModel = RegistViewModel(), userViewModel = UserViewModel(), navController = rememberNavController())
}

