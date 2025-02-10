package com.example.gogoma.ui.navigation

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gogoma.ui.components.BottomBar
import com.example.gogoma.ui.components.BottomSheet
import com.example.gogoma.ui.screens.MypageScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.gogoma.R
import com.example.gogoma.ui.components.BottomSheetContentWithTitle
import com.example.gogoma.ui.components.FilterListItemContent
import com.example.gogoma.ui.components.FilterListItemSelect
import com.example.gogoma.ui.components.FilterListItemTitle
import com.example.gogoma.ui.components.TopBarArrow
import com.example.gogoma.ui.screens.AddressSelectionScreen
import com.example.gogoma.ui.screens.MarathonDetailScreen
import com.example.gogoma.ui.screens.PaymentScreen
import com.example.gogoma.ui.screens.PaymentStatusScreen
import com.example.gogoma.ui.screens.RegistDetailsScreen
import com.example.gogoma.ui.screens.RegistListScreen
import com.example.gogoma.ui.screens.SignScreen
import com.example.gogoma.ui.screens.SignUpScreen
import com.example.gogoma.viewmodel.BottomSheetViewModel
import com.example.gogoma.viewmodel.MarathonListViewModel
import com.example.gogoma.viewmodel.PaymentViewModel
import com.example.gogoma.viewmodel.UserViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.gogoma.ui.screens.FriendListScreen
import com.example.gogoma.ui.screens.PaceScreen
import com.example.gogoma.ui.screens.PaymentWebViewScreen
import com.example.gogoma.ui.screens.WatchConnectScreen
import com.example.gogoma.viewmodel.FriendsViewModel
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Composable
fun AppNavigation(userViewModel: UserViewModel){
    val navController = rememberNavController()
    val bottomSheetViewModel : BottomSheetViewModel = viewModel()
    val marathonListViewModel: MarathonListViewModel = viewModel()
    val paymentViewModel: PaymentViewModel = viewModel()
    val friendsViewModel: FriendsViewModel = viewModel()

    // 로그인 상태 감지
    LaunchedEffect(userViewModel.loginStatus) {
        if (userViewModel.loginStatus == "signup") {
            navController.navigate("signup") // signup 상태일 때 회원가입 화면으로 이동
        }
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

    NavHost(
        navController = navController,
        startDestination = "splash"
    ){
        composable("splash") {
            //스플래시 화면
            SplashScreen(navController = navController)
        }

        composable("paceSetting") {
            PaceScreen (navController = navController, userViewModel, bottomSheetViewModel)
        }

        composable("watchConnect") {
            WatchConnectScreen()
        }

        composable("main") {
            Scaffold (
                topBar = { TopBar() },
                bottomBar = { BottomBar(navController = navController, userViewModel) }
            ){ paddingValues ->
                Box(modifier = Modifier.fillMaxSize().padding(paddingValues)){
                    MainScreen(
                        navController,
                        marathonListViewModel = marathonListViewModel,
                        onFilterClick = { filter ->
                            if(!bottomSheetViewModel.isSubPageVisible){//하위페이지가 아니라면
                                marathonListViewModel.updatePendingFilter(
                                    marathonListViewModel.selectedFilters.city,
                                    marathonListViewModel.selectedFilters.marathonStatus,
                                    marathonListViewModel.selectedFilters.year,
                                    marathonListViewModel.selectedFilters.month,
                                    marathonListViewModel.selectedFilters.courseTypeList,
                                )
                            }
                                bottomSheetViewModel.showBottomSheet(filter) //Bottom Sheet 보이기
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
                topBar = {
                    TopBarArrow (
                        title = "신청 내역",
                        onBackClick = { navController.popBackStack() }
                    )
                },
                bottomBar = { BottomBar(navController = navController, userViewModel) }
            ){ paddingValues ->
                Box(modifier = Modifier.fillMaxSize().padding(paddingValues)){
                    RegistListScreen(
                        navController,
                        onRegistClick = { registId ->
                            navController.navigate("registDetail/$registId")
                        }
                    )
                }

            }
        }

        composable(
            route = "registDetail/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val registId = backStackEntry.arguments?.getInt("id")
            registId?.let {
                RegistDetailsScreen(registId = it, navController = navController, userViewModel)
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
            MypageScreen(navController, userViewModel)
        }

        composable("signpage") {
            SignScreen(navController, userViewModel)
        }

        composable("signup") {
            SignUpScreen(navController = navController, userViewModel = userViewModel)
        }

        // 결제 성공 화면
        composable(
            "paymentSuccess/{registJson}",
            arguments = listOf(navArgument("registJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val registJsonEncoded = backStackEntry.arguments?.getString("registJson")
            val registJson = registJsonEncoded?.let { URLDecoder.decode(it, StandardCharsets.UTF_8.toString()) }
            PaymentStatusScreen(isSuccess = true, registJson = registJson, onConfirm = { navController.navigate("main") })
        }

        // 결제 실패 화면
        composable("paymentFailure") {
            PaymentStatusScreen(
                isSuccess = false,
                registJson = null,  // 실패 시에는 registJson을 넘기지 않음
                onConfirm = { navController.popBackStack() },
                onNavigateToMain = { navController.navigate("main") }
            )
        }

        composable("paymentWebViewScreen") {
            val paymentUrl = navController.previousBackStackEntry?.savedStateHandle?.get<String>("paymentUrl") ?: ""
            val registJson = navController.previousBackStackEntry?.savedStateHandle?.get<String>("registJson") ?: ""

            PaymentWebViewScreen(
                navController = navController,
                paymentUrl = paymentUrl,
                viewModel = paymentViewModel,
                registJson = registJson
            )
            val pgToken = Uri.parse(paymentUrl).getQueryParameter("pg_token") ?: ""
            if (pgToken.isNotEmpty()) {
                paymentViewModel.handlePaymentRedirect(pgToken)
            }

        }

        composable("friendList") {
            FriendListScreen(navController, userViewModel, friendsViewModel)
        }
    }


    val mainBottomSheetContent : @Composable () -> Unit = {
        // BottomSheetContentWithTitle 사용
        BottomSheetContentWithTitle (
            title = "정렬",
            headerLeftContent = {
                if(bottomSheetViewModel.isSubPageVisible){
                    IconButton(
                        onClick = { bottomSheetViewModel.goBackToPreviousPage() },
                        modifier = Modifier.size(20.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_back),
                            contentDescription = "Back Arrow",
                            tint = Color.Black,
                        )
                    }
                }else{
                    Text(
                        text = "닫기",
                        modifier = Modifier.clickable {
                            bottomSheetViewModel.hideBottomSheet()
                        }
                    )
                }
            },
            bottomButton = {
                if(bottomSheetViewModel.pageName=="기본"){
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp),
                        onClick = {
                            marathonListViewModel.applyFilters()
                            bottomSheetViewModel.hideBottomSheet()
                        }
                    ) {
                        Text("대회 보기")
                    }
                }
            }
        ) {

            val filterTitles = marathonListViewModel.filterTitles
            val filterContents = marathonListViewModel.filterContents

            //하단 필터 내용
            LazyColumn {
                if(bottomSheetViewModel.pageName == "기본"){
                    items(filterTitles) { title ->
                        val filterContent = when (title) {
                            "지역" -> marathonListViewModel.pendingFilters.city ?: "모든 지역"
                            "접수 상태" -> marathonListViewModel.pendingFilters.marathonStatus ?: "모든 접수 상태"
                            "종목" -> marathonListViewModel.pendingFilters.courseTypeList?.joinToString(", ") ?: "모든 종목"
                            "년도" -> marathonListViewModel.pendingFilters.year ?: "모든 년도"
                            "월" -> marathonListViewModel.pendingFilters.month ?: "모든 월"
                            else -> "모든 ${title}"
                        }

                        FilterListItemSelect(title, filterContent) {
                            bottomSheetViewModel.showSubPage(title, "필터")
                        }
                    }
                } else {
                    val contentList = filterContents[bottomSheetViewModel.pageName]
                    if (contentList != null) {
                        item {
                            FilterListItemTitle(bottomSheetViewModel.pageName)
                        }
                        item {
                            FilterListItemContent("전체", onClick = {
                                if(bottomSheetViewModel.isSubPageVisible){//기본 페이지에서 들어간 경우
                                    when (bottomSheetViewModel.pageName) {
                                        "지역" -> marathonListViewModel.updatePendingFilter(city = null)
                                        "접수 상태" -> marathonListViewModel.updatePendingFilter(marathonStatus = null)
                                        "종목" -> marathonListViewModel.updatePendingFilter(courseTypeList = null)
                                        "년도" -> marathonListViewModel.updatePendingFilter(year = null)
                                        "월" -> marathonListViewModel.updatePendingFilter(month = null)
                                    }

                                    // 이전 모달창으로 돌아가기
                                    bottomSheetViewModel.goBackToPreviousPage()
                                }else{//하위 페이지에 바로 들어간 경우
                                    // 필터 값 업데이트
                                    when (bottomSheetViewModel.pageName) {
                                        "지역" -> marathonListViewModel.updateFilters(city = null)
                                        "접수 상태" -> marathonListViewModel.updateFilters(marathonStatus = null)
                                        "종목" -> marathonListViewModel.updateFilters(courseTypeList = null)
                                        "년도" -> marathonListViewModel.updateFilters(year = null)
                                        "월" -> marathonListViewModel.updateFilters(month = null)
                                    }

                                    // 모달창 닫기
                                    bottomSheetViewModel.hideBottomSheet()
                                }
                            })
                        }
                        items(contentList) { content ->
                            FilterListItemContent(content, onClick = {

                                if(bottomSheetViewModel.isSubPageVisible){//기본 페이지에서 들어간 경우
                                    when (bottomSheetViewModel.pageName) {
                                        "지역" -> marathonListViewModel.updatePendingFilter(city = content)
                                        "접수 상태" -> marathonListViewModel.updatePendingFilter(marathonStatus = content)
                                        "종목" -> marathonListViewModel.updatePendingFilter(courseTypeList = listOf(content))
                                        "년도" -> marathonListViewModel.updatePendingFilter(year = content)
                                        "월" -> marathonListViewModel.updatePendingFilter(month = content)
                                    }

                                    // 이전 모달창으로 돌아가기
                                    bottomSheetViewModel.goBackToPreviousPage()
                                }else{//하위 페이지에 바로 들어간 경우
                                    // 필터 값 업데이트
                                    when (bottomSheetViewModel.pageName) {
                                        "지역" -> marathonListViewModel.updateFilters(city = content)
                                        "접수 상태" -> marathonListViewModel.updateFilters(marathonStatus = content)
                                        "종목" -> marathonListViewModel.updateFilters(courseTypeList = listOf(content))
                                        "년도" -> marathonListViewModel.updateFilters(year = content)
                                        "월" -> marathonListViewModel.updateFilters(month = content)
                                    }

                                    // 모달창 닫기
                                    bottomSheetViewModel.hideBottomSheet()
                                }

                            })
                        }
                    } else {
                        item {
                            Text("잘못된 필터 값입니다.")
                        }
                    }
                }
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
