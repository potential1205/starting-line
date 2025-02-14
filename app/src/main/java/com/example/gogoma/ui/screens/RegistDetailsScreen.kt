package com.example.gogoma.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.gogoma.ui.components.BottomBar
import com.example.gogoma.ui.components.MarathonDetailItem
import com.example.gogoma.ui.components.PaymentDetails
import com.example.gogoma.ui.components.TopBarArrow
import com.example.gogoma.utils.TokenManager
import com.example.gogoma.viewmodel.MarathonDetailViewModel
import com.example.gogoma.viewmodel.RegistDetailViewModel
import com.example.gogoma.viewmodel.UserViewModel

@Composable
fun RegistDetailsScreen(registId: Int, navController: NavController, userViewModel: UserViewModel, ) {
    val registDetailViewModel: RegistDetailViewModel = viewModel()
    val marathonDetailViewModel: MarathonDetailViewModel = viewModel()

    val context = LocalContext.current
    val userMarathonDetail by registDetailViewModel.userMarathonDetail.collectAsState()
    val marathonDetail by marathonDetailViewModel.marathonDetail.collectAsState()

    // 유저 마라톤 상세 정보 로드
    LaunchedEffect(registId) {
        val token = TokenManager.getAccessToken(context)
        token?.let { registDetailViewModel.getUserMarathonById(it, registId) }
    }

    LaunchedEffect(userMarathonDetail?.marathon?.id) {
        userMarathonDetail?.marathon?.id?.let { marathonDetailViewModel.loadMarathonDetail(it) }
    }

    Scaffold(
        topBar = { TopBarArrow(title = "신청 상세", { navController.popBackStack() } ) },
        bottomBar = { BottomBar(navController, userViewModel) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()) // 스크롤 추가
        ) {
            // 마라톤 상세 정보
            marathonDetail?.let { MarathonDetailItem(it) }

            // 결제 내역
            userMarathonDetail?.let { PaymentDetails(it) }
        }
    }
}

@Preview
@Composable
fun RegistDetailsScreenPreview() {
    val navController = rememberNavController()
    RegistDetailsScreen(1, navController, userViewModel = UserViewModel())
}