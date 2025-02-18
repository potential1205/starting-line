package com.example.gogoma.presentation.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.gogoma.presentation.pager.ViewPagerScreen
import com.example.gogoma.presentation.screens.StartScreen
import com.example.gogoma.presentation.viewmodel.MarathonDataViewModel

@SuppressLint("ContextCastToActivity")
@Composable
fun AppNavigation(navController: NavHostController, marathonDataViewModel: MarathonDataViewModel) {
//    val activity = LocalContext.current as? FragmentActivity
//        ?: throw IllegalStateException("AppNavigation must be used within a FragmentActivity")
//
//    // 앱 시작 시 한 번만 데이터 리스너 등록
//    LaunchedEffect(Unit) {
//        marathonDataViewModel.startDataListener(activity)
//    }

    NavHost(
        navController = navController,
        startDestination = "startScreen"

    ) {
        composable("startScreen") { StartScreen(navController, marathonDataViewModel) }
        composable("viewPagerScreen") {
            ViewPagerScreen(activity = LocalContext.current as FragmentActivity, navController, marathonDataViewModel)
        }
    }
}