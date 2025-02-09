package com.example.gogoma.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.gogoma.theme.BrandColor1
import com.example.gogoma.theme.BrandColor2
import com.example.gogoma.ui.components.*
import com.google.gson.Gson

@Composable
fun PaymentStatusScreen(
    isSuccess: Boolean,
    registJson: String? = null,  // 🔥 여기서 기본값을 null로 설정
    onConfirm: () -> Unit,
    onNavigateToMain: (() -> Unit)? = null
) {
    val gson = remember { Gson() }

    // ✅ JSON -> Regist 객체 변환 (실패 화면에서는 JSON이 없을 수 있음)
    val regist: Regist? = registJson?.let {
        try {
            gson.fromJson(it, Regist::class.java).apply {
                Log.d("PaymentStatusScreen", "✅ JSON 변환 성공: $this")
            }
        } catch (e: Exception) {
            Log.e("PaymentStatusScreen", "❌ JSON 변환 실패: ${e.message}")
            null
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
//        if (isSuccess) {
//            TopBarArrow("신청 완료", {})
//        } else {
//            TopBar()
//        }
        TopBarArrow(title = if (isSuccess) "신청 완료" else "결제 실패", onBackClick = {})

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier.weight(1f).fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (isSuccess) {
                SuccessContent(onConfirm, regist)
            } else {
                FailureContent(onConfirm, onNavigateToMain)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

//        if (isSuccess) {
//            BottomBarButton(
//                text = "완료",
//                backgroundColor = BrandColor1,
//                textColor = Color.White,
//                onClick = onConfirm
//            )
//        } else {
////            BottomBar(navController = rememberNavController())
//        }
        BottomBarButton(
            text = "완료",
            backgroundColor = BrandColor1,
            textColor = Color.White,
            onClick = onConfirm
        )

    }
}



@Composable
fun SuccessContent(onConfirm: () -> Unit, registInfo: Regist?) {
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
                Regist(
                    registrationDate = it.registrationDate,
                    title = it.title,
                    date = it.date,
                    distance = it.distance
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
