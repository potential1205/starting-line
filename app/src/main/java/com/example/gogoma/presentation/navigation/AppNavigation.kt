package com.example.gogoma.presentation.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.gogoma.presentation.pager.ViewPagerScreen
import com.example.gogoma.presentation.screens.StartScreen

@SuppressLint("ContextCastToActivity")
@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "startScreen"

    ) {
        composable("startScreen") { StartScreen(navController) }
        composable("viewPagerScreen") {
            ViewPagerScreen(activity = LocalContext.current as FragmentActivity, navController)
        }
    }
}