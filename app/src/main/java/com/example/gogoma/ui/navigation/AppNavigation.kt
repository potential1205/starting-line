package com.example.gogoma.ui.navigation

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gogoma.ui.screens.MainScreen
import androidx.compose.runtime.LaunchedEffect
import com.example.gogoma.ui.components.bottomsheets.BottomSheet
import com.example.gogoma.ui.screens.MypageScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
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
import androidx.compose.ui.platform.LocalContext
import com.example.gogoma.GlobalApplication
import com.example.gogoma.ui.components.bottomsheets.MainBottomSheetContent
import com.example.gogoma.ui.components.bottomsheets.PaceSettingBottomSheetContent
import com.example.gogoma.ui.screens.FriendListScreen
import com.example.gogoma.ui.screens.PaceScreen
import com.example.gogoma.ui.screens.PaymentWebViewScreen
import com.example.gogoma.ui.screens.WatchConnectScreen
import com.example.gogoma.viewmodel.FriendsViewModel
import com.example.gogoma.viewmodel.PaceViewModel
import com.example.gogoma.viewmodel.PaceViewModelFactory
import com.example.gogoma.viewmodel.RegistViewModel
import com.example.gogoma.viewmodel.ScrollViewModel
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
    val scrollViewModel: ScrollViewModel = viewModel()

    val protectedRouted = listOf("registList", "paceSetting", "watchConnect", "friendList", "mypage")

    // 로그인 상태 감지
    LaunchedEffect(userViewModel.loginStatus) {
        if (userViewModel.loginStatus == "signup") {
            navController.navigate("signup") // signup 상태일 때 회원가입 화면으로 이동
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
                        "registList" -> RegistListScreen(
                            navController,
                            userViewModel = userViewModel,
                            registViewModel = registViewModel,
                            onRegistClick = { registId ->
                                navController.navigate("registDetail/$registId")
                            }
                        )
                        "paceSetting" -> PaceScreen (navController = navController, userViewModel, bottomSheetViewModel, paceViewModel)
                        "watchConnect" -> WatchConnectScreen(navController, userViewModel)
                        "friendList" -> FriendListScreen(navController, userViewModel, friendsViewModel)
                        "mypage" -> MypageScreen(navController, userViewModel)

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

        composable("main") {
            MainScreen(
                navController,
                userViewModel = userViewModel,
                marathonListViewModel = marathonListViewModel,
                bottomSheetViewModel = bottomSheetViewModel,
                scrollViewModel = scrollViewModel,
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

    // 바텀 시트 표시
    BottomSheet(
        isVisible = bottomSheetViewModel.isBottomSheetVisible,
        onDismiss = { bottomSheetViewModel.hideBottomSheet() },
        content = {
            when (navController.currentDestination?.route) {
                "main" -> MainBottomSheetContent(bottomSheetViewModel, marathonListViewModel)
                "paceSetting" -> PaceSettingBottomSheetContent(bottomSheetViewModel, paceViewModel)
                else -> Unit // 기본 content는 없게 설정
            }
        }
    )
}
