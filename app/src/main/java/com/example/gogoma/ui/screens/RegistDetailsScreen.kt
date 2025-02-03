package com.example.gogoma.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.gogoma.data.model.PaymentDetail
import com.example.gogoma.ui.components.BottomBar
import com.example.gogoma.ui.components.PaymentDetails
import com.example.gogoma.ui.components.TopBarArrow

@Composable
fun RegistDetailsScreen(registId: Int, navController: NavController) {
    val paymentDetails = PaymentDetail(
        paymentDate = "2025-01-29",
        paymentType = "신용카드",
        paymentAmount = "50,000원",
        address = "서울특별시 강남구 테헤란로 123",
        raceCategory = "5km",
        gift = "반팔(95)"
    )

    Scaffold(
        topBar = { TopBarArrow(title = "신청 상세") { navController.popBackStack() } },
        bottomBar = { BottomBar(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()) // 스크롤 추가
        ) {
            // 마라톤 상세 정보


            // 결제 내역
            PaymentDetails(paymentDetails)
        }
    }
}

@Preview
@Composable
fun RegistDetailsScreenPreview() {
    val navController = rememberNavController()
    RegistDetailsScreen(1, navController)
}