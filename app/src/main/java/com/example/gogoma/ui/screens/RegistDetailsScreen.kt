package com.example.gogoma.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.gogoma.theme.GogomaTheme
import com.example.gogoma.ui.components.BottomBar
import com.example.gogoma.ui.components.MarathonDetailItem
import com.example.gogoma.ui.components.PaymentDetails
import com.example.gogoma.ui.components.TopBarArrow
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

    // 현재 선택된 탭 (0: 대회 정보, 1: 결제 내역)
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabTitles = listOf("대회 정보", "결제 내역")

    // 유저 마라톤 상세 정보 로드
    LaunchedEffect(registId) {
//        val token = TokenManager.getAccessToken(context)
        val token = "qnCHwQWZQ1QCP8x1RPK5ZqkN9h5ieH_gAAAAAQo9c04AAAGVEnkGfpCBbdpZdq0Z"
        token?.let { registDetailViewModel.getUserMarathonById(it, registId) }
    }

    LaunchedEffect(userMarathonDetail?.marathon?.id) {
        userMarathonDetail?.marathon?.id?.let { marathonDetailViewModel.loadMarathonDetail(it) }
    }

    Scaffold(
        topBar = { TopBarArrow(title = marathonDetail?.marathon?.title.toString(), { navController.popBackStack() } ) },
        bottomBar = { BottomBar(navController, userViewModel) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()) // 스크롤 추가
        ) {
            // 탭 레이아웃
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(horizontal = 20.dp)
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title) }
                    )
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
                    0 -> marathonDetail?.let { MarathonDetailItem(it) }
                    // 결제 내역
                    1 -> userMarathonDetail?.let { PaymentDetails(it) }
                }
            }
        }
    }
}

@Preview
@Composable
fun RegistDetailsScreenPreview() {
    GogomaTheme {
        val navController = rememberNavController()
        RegistDetailsScreen(1, navController, userViewModel = UserViewModel())
    }
}