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
import androidx.compose.ui.platform.LocalContext
import com.example.gogoma.GlobalApplication
import com.example.gogoma.ui.screens.FriendListScreen
import com.example.gogoma.ui.screens.PaceScreen
import com.example.gogoma.ui.screens.PaymentWebViewScreen
import com.example.gogoma.ui.screens.WatchConnectScreen
import com.example.gogoma.utils.TokenManager
import com.example.gogoma.viewmodel.FriendsViewModel
import com.example.gogoma.viewmodel.PaceViewModel
import com.example.gogoma.viewmodel.PaceViewModelFactory
import com.example.gogoma.viewmodel.RegistViewModel
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Composable
fun AppNavigation(userViewModel: UserViewModel){
    val context = LocalContext.current
    val navController = rememberNavController()
    val bottomSheetViewModel : BottomSheetViewModel = viewModel()
    val marathonListViewModel: MarathonListViewModel = viewModel()
    val paymentViewModel: PaymentViewModel = viewModel()
    val friendsViewModel: FriendsViewModel = viewModel()
    val registViewModel: RegistViewModel = viewModel()
    val globalApplication = context.applicationContext as GlobalApplication
    val paceViewModel: PaceViewModel = viewModel(factory = PaceViewModelFactory(globalApplication))

    val protectedRouted = listOf("mypage", "friendList")

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
        startDestination = "main"
    ){

        composable("signpage") {
            SignScreen(navController, userViewModel)
        }

        composable("signup") {
            SignUpScreen(navController = navController, userViewModel = userViewModel)
        }

        //로그인 해야 접근 가능한 페이지
        protectedRouted.forEach { route ->
            composable(route) { backStackEntry ->
                if(userViewModel.isLoggedIn){
                    when (route) {
                        "mypage" -> MypageScreen(navController, userViewModel)
                        "paceSetting" -> PaceScreen (navController = navController, userViewModel, bottomSheetViewModel, paceViewModel)
                        "watchConnect" -> WatchConnectScreen(navController, userViewModel)
                        "friendList" -> FriendListScreen(navController, userViewModel, friendsViewModel)

                    }
                } else {
                    LaunchedEffect(Unit) {
                        navController.navigate("signpage"){
                            // 로그인 페이지를 뒤로 가기 스택에서 제거
                            popUpTo("signpage") { inclusive = true }
                        }
                    }
                }
            }
        }

        composable("paceSetting") {
            PaceScreen (navController = navController, userViewModel, bottomSheetViewModel, paceViewModel)
        }

        composable("watchConnect") {
            WatchConnectScreen(navController, userViewModel)
        }

        composable("main") {
            Scaffold (
                topBar = { TopBar() },
                bottomBar = { BottomBar(navController = navController, userViewModel) }
            ){ paddingValues ->
                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)){
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
                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)){
                    RegistListScreen(
                        navController,
                        registViewModel = registViewModel,
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

        // 결제 성공 화면
        composable(
            "paymentSuccess/{registJson}",
            arguments = listOf(navArgument("registJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val registJsonEncoded = backStackEntry.arguments?.getString("registJson")
            val registJson = registJsonEncoded?.let { URLDecoder.decode(it, StandardCharsets.UTF_8.toString()) }
            PaymentStatusScreen(isSuccess = true, registJson = registJson, viewModel = registViewModel, onConfirm = { navController.navigate("main") })
        }

        // 결제 실패 화면
        composable("paymentFailure") {
            PaymentStatusScreen(
                isSuccess = false,
                registJson = null,  // 실패 시에는 registJson을 넘기지 않음
                viewModel = registViewModel,
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
                paymentViewModel.handlePaymentRedirect(pgToken, context)
            }

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

    val paceSettingBottomSheetContent : @Composable () -> Unit = {
        BottomSheetContentWithTitle (
            title = "목표 페이스",
            headerLeftContent = {
                Text(
                    text = "닫기",
                    modifier = Modifier.clickable {
                        bottomSheetViewModel.hideBottomSheet()
                    }
                )
            },
        ) {
            val paceMap = mapOf(
                300 to "3:00 세계적 수준 - 극히 빠른 페이스",
                330 to "3:30 올림픽급 – 최상위 경쟁자 수준",
                400 to "4:00 엘리트 – 프로급의 뛰어난 능력",
                430 to "4:30 프로 – 전문 선수에 가까운 속도",
                500 to "5:00 챔피언 – 경쟁력 있는 주자",
                530 to "5:30 도전자 – 우승을 노릴 만한 페이스",
                600 to "6:00 페이스 세터 – 기준을 제시하는 주자",
                630 to "6:30 민첩한 – 발이 빠른 주자",
                700 to "7:00 꾸준한 – 일정한 보폭을 유지하는 주자",
                730 to "7:30 탄탄한 – 신뢰할 수 있는 달리기",
                800 to "8:00 시간 관리 – 자신의 페이스를 잘 지키는",
                830 to "8:30 일관된 – 변함없이 꾸준한 페이스",
                900 to "9:00 믿음직한 – 안정적인 러닝 스타일",
                930 to "9:30 일상 주자 – 누구나 할 수 있는 속도",
                1000 to "10:00 균형 잡힌 – 무리하지 않고 달리는",
                1030 to "10:30 인내심 있는 – 오랜 시간 달릴 수 있는",
                1100 to "11:00 회복력이 좋은 – 역경을 이겨내는 주자",
                1130 to "11:30 주말 전사 – 가볍게 즐기는 러너",
                1200 to "12:00 부담 없는 – 여유로운 조깅 페이스",
                1230 to "12:30 편안하게 달리는 – 느긋한 주행 스타일",
                1300 to "13:00 한가롭게 – 여유로운 크루징 페이스",
                1330 to "13:30 느긋한 – 산책하듯 편안한 페이스"
            )

            LazyColumn {
                item {
                    FilterListItemTitle("/km")
                }
                items(paceMap.entries.toList()) { (value, text) ->
                    FilterListItemContent(text, onClick = {
                        paceViewModel.marathonStartInitDataResponse?.let {
                            paceViewModel.patchMarathonPace(TokenManager.getAccessToken(context).toString(), it.marathonId, value)
                        }
                        // 모달창 닫기
                        bottomSheetViewModel.hideBottomSheet()

                    })
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
                "paceSetting" -> paceSettingBottomSheetContent()
                else -> Unit // 기본 content는 없게 설정
            }
        }
    )
}
