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
    NavHost(
        navController = navController,
        startDestination = "startScreen"

    ) {
        composable("startScreen") { StartScreen(navController, marathonDataViewModel) }
        composable("viewPagerScreen") {
            ViewPagerScreen(activity = LocalContext.current as FragmentActivity)
        }
    }
}