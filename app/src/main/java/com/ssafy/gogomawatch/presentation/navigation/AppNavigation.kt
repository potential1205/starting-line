package com.ssafy.gogomawatch.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ssafy.gogomawatch.presentation.screens.PersonalScreen
import com.ssafy.gogomawatch.presentation.screens.StartScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "startScreen") {
        composable("startScreen") { StartScreen(navController) }
        composable("personalScreen") { PersonalScreen() }
    }
}