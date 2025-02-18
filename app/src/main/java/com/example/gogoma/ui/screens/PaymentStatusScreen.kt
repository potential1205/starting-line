package com.example.gogoma.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gogoma.R
import com.example.gogoma.data.dto.UserMarathonSearchDto
import com.example.gogoma.theme.BrandColor1
import com.example.gogoma.theme.BrandColor2
import com.example.gogoma.ui.components.*
import com.example.gogoma.utils.TokenManager
import com.example.gogoma.viewmodel.RegistViewModel
import com.google.gson.Gson

@Composable
fun PaymentStatusScreen(
    isSuccess: Boolean,
    registJson: String? = null,
    viewModel: RegistViewModel,
    onConfirm: () -> Unit,
    onNavigateToMain: (() -> Unit)? = null
) {
    val context = LocalContext.current
    val gson = remember { Gson() }
    val regist: UserMarathonSearchDto? = registJson?.let {
        try {
            val parsed = gson.fromJson(it, UserMarathonSearchDto::class.java)
            gson.fromJson(it, UserMarathonSearchDto::class.java).apply {
                Log.d("PaymentStatusScreen", "✅ JSON 변환 성공: $this")
            }
        } catch (e: Exception) {
            Log.e("PaymentStatusScreen", "❌ JSON 변환 실패: ${e.message}")
            null
        }
    }

    LaunchedEffect(isSuccess) {
        if (isSuccess && regist != null) {
            viewModel.addRegist(regist) // 결제 성공 시 리스트에 추가
        }
    }

    LaunchedEffect(isSuccess) {
        if (isSuccess && regist != null) {
            viewModel.notifyFriends(TokenManager.getAccessToken(context = context).toString(), marathonId = regist.userMarathonId!!)
        }
    }

    Scaffold(
        topBar = { TopBarArrow(title = if (isSuccess) "신청 완료" else "결제 실패", onBackClick = {}) },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(16.dp)
            ) {
                BottomBarButton(
                    text = "완료",
                    backgroundColor = BrandColor1,
                    textColor = Color.White,
                    onClick = onConfirm
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ContentLayout(isSuccess, regist)
        }
    }
}

@Composable
fun SuccessContent(onConfirm: () -> Unit, registInfo: UserMarathonSearchDto?) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "✅ 신청이 완료되었습니다.",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

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
                onClick = {}
            )
            Spacer(modifier = Modifier.height(16.dp))
        } ?: run {
            Log.e("SuccessContent", "❌ registInfo가 null입니다.")
        }
    }
}

@Composable
fun FailureContent(
    onConfirm: () -> Unit,
    onNavigateToMain: (() -> Unit)?
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "❌ 신청에 실패했습니다.",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onConfirm,
            colors = ButtonDefaults.buttonColors(containerColor = BrandColor1),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "결제 페이지로 돌아가기", color = Color.White, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (onNavigateToMain != null) {
            Button(
                onClick = onNavigateToMain,
                colors = ButtonDefaults.buttonColors(containerColor = BrandColor2),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "메인 페이지로 돌아가기", color = Color.White, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun ContentLayout (isSuccess: Boolean, registInfo: UserMarathonSearchDto?) {

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
    LazyColumn (
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 40.dp, top = 110.dp, end = 40.dp, bottom = 110.dp),
        verticalArrangement = Arrangement.spacedBy(25.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            Icon(
                painter = painter,
                contentDescription = "payment status icon",
                tint = pointColor,
                modifier = Modifier
                    .size(110.dp)
            )
        }
        item {
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
                    text = if(isSuccess) "무사히 첫 출발을\n해냈습니다" else "결제에 실패했습니다.",
                    style = TextStyle(
                        fontSize = 13.5.sp,
                        lineHeight = 21.6.sp,
                        fontWeight = FontWeight.Light,
                        color = Color(0xFF1C1C1C),
                        textAlign = TextAlign.Center,
                    )
                )
            }
        }
        item {
            if(registInfo != null){
                RegistListItem(registInfo, onClick = {}, background = Color.Transparent)
            }
        }
        item {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Button(
                    onClick = {
                        // 버튼 클릭 시 실행될 동작 추가
                    },
                    modifier = Modifier
                        .width(332.dp)
                        .height(44.dp)
                        .background(color = Color(0xFF2680FF), shape = RoundedCornerShape(size = 4.dp))
                        .padding(start = 18.dp, top = 10.dp, end = 18.dp, bottom = 10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2680FF),
                        contentColor = Color.White
                    )
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_list),
                            contentDescription = "list icon",
                        )
                        Text(
                            text = "신청 내역으로",
                            style = TextStyle(
                                fontSize = 15.sp,
                                fontWeight = FontWeight(800),
                                color = Color(0xFFFEFEFE),
                            )
                        )
                    }
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ContentLayoutPreview () {
    ContentLayout(true, UserMarathonSearchDto(
        userMarathonId = 1,
        marathonTitle = "서울 마라톤 2025",
        marathonType = 1,
        dday = "D-7",
        raceStartDateTime = "2025-03-15T08:00:00",
        paymentDateTime = "2025-01-10T14:30:00"
    )
    )
}

