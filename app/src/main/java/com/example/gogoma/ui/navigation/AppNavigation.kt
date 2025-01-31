package com.example.gogoma.ui.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gogoma.ui.components.BottomBar
import com.example.gogoma.ui.components.BottomSheet
import com.example.gogoma.ui.screens.MypageScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gogoma.ui.components.BottomSheetContentWithTitle
import com.example.gogoma.viewmodel.BottomSheetViewModel

@Composable
fun AppNavigation(){
    val navController = rememberNavController()
    val bottomSheetViewModel : BottomSheetViewModel = viewModel()

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
                MainScreen(
                    navController,
                    modifier = Modifier
                    .padding(paddingValues),
                    onFilterClick = { filter ->
                        bottomSheetViewModel.selectFilter(filter)
                        bottomSheetViewModel.showBottomSheet() //Bottom Sheet 보이기
                    }
                )
            }
        }

        composable("mypage") {
            // 마이페이지 화면
            MypageScreen(navController = navController)
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
fun AppNavigationPreview() {
    AppNavigation()
}
