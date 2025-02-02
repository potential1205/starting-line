package com.example.gogoma.ui.navigation

import PaymentStatusScreen
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gogoma.ui.components.TopBar
import com.example.gogoma.ui.screens.MainScreen
import com.example.gogoma.ui.screens.SplashScreen
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gogoma.ui.components.BottomBar
import com.example.gogoma.ui.components.BottomSheet
import com.example.gogoma.ui.screens.MypageScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.gogoma.ui.components.BottomBarButton
import com.example.gogoma.ui.components.BottomSheetContentWithTitle
import com.example.gogoma.ui.components.TopBarArrow
import com.example.gogoma.ui.screens.AddressSelectionScreen
import com.example.gogoma.ui.screens.MarathonDetailScreen
import com.example.gogoma.ui.screens.PaymentScreen
import com.example.gogoma.ui.screens.RegistListScreen
import com.example.gogoma.viewmodel.BottomSheetViewModel
import com.example.gogoma.viewmodel.PaymentViewModel

@Composable
fun AppNavigationJuho(){
    val navController = rememberNavController()
    val bottomSheetViewModel : BottomSheetViewModel = viewModel()
    val paymentViewModel: PaymentViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ){
        composable("splash") {
            //스플래시 화면
            SplashScreen(navController = navController)
        }

        composable("main") {
            Scaffold (
                topBar = { TopBar() },
                bottomBar = { BottomBar(navController = navController) }
            ){ paddingValues ->
                Box(modifier = Modifier.fillMaxSize().padding(paddingValues)){
                    MainScreen(
                        navController,
                        onFilterClick = { filter ->
                            bottomSheetViewModel.selectFilter(filter)
                            bottomSheetViewModel.showBottomSheet() //Bottom Sheet 보이기
                        },
                        onMarathonClick = { marathonId ->
                            // 마라톤 클릭 시 상세 페이지로 이동
                            navController.navigate("marathonDetail/$marathonId")
                        }
                    )
                }

            }
        }

        composable("registList") {
            Scaffold (
                topBar = { TopBarArrow (
                    title = "신청 내역",
                    onBackClick = { navController.popBackStack() }
                )
                },
                bottomBar = { BottomBar(navController = navController) }
            ){ paddingValues ->
                Box(modifier = Modifier.fillMaxSize().padding(paddingValues)){
                    RegistListScreen(navController)
                }

            }
        }

        composable(
            route = "marathonDetail/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val marathonId = backStackEntry.arguments?.getInt("id")
            marathonId?.let {
                MarathonDetailScreen(marathonId = it, navController = navController)
            }
        }

        composable(
            route = "payment/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val marathonId = backStackEntry.arguments?.getInt("id")
            marathonId?.let {
                PaymentScreen(navController = navController, marathonId = it, viewModel = paymentViewModel)
            }
        }

        // 주소 선택 화면
        composable("addressSelection") {
            AddressSelectionScreen(navController = navController, viewModel = paymentViewModel)
        }

        composable("mypage") {
            Scaffold (
                topBar = { TopBarArrow (
                    title = "마이 페이지",
                    onBackClick = { navController.popBackStack() }
                )
                },
                bottomBar = { BottomBar(navController = navController) }
            ){ paddingValues ->
                Box(modifier = Modifier.fillMaxSize().padding(paddingValues)){
                    MypageScreen(navController = navController)
                }

            }
        }

        // 결제 성공 화면
        composable("paymentSuccess") {
            PaymentStatusScreen(isSuccess = true, onConfirm = { navController.navigate("main") })
        }

        // 결제 실패 화면
        composable("paymentFailure") {
            PaymentStatusScreen(isSuccess = false, onConfirm = { navController.popBackStack("payment/{id}", inclusive = false) })
        }
    }


    val mainBottomSheetContent : @Composable () -> Unit = {
        // BottomSheetContentWithTitle 사용
        BottomSheetContentWithTitle (
            title = "정렬",
            headerLeftContent = {
                Text(
                    text = "닫기",
                    modifier = Modifier.clickable {
                        bottomSheetViewModel.hideBottomSheet()
                    }
                )
            },
            headerRightContent = {
                // 여기에 원하는 오른쪽 아이콘 또는 버튼을 넣으세요
            }
        ) {
            // 하단 내용 부분
            Column(modifier = Modifier.padding(16.dp)) {
                Text("선택된 필터: ${bottomSheetViewModel.selectedFilter}")
                Spacer(modifier = Modifier.height(8.dp))
                Text("-Bottom Sheet 작동 확인-")
            }
        }
    }

    // 바텀 시트 표시
    BottomSheet(
        isVisible = bottomSheetViewModel.isBottomSheetVisible,
        onDismiss = { bottomSheetViewModel.hideBottomSheet() },
        content = {
            when (navController.currentDestination?.route) {
                "main" -> mainBottomSheetContent()
                else -> Unit // 기본 content는 없게 설정
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun AppNavigationJuhoPreview() {
    AppNavigationJuho()
}
