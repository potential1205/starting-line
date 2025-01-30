package com.example.gogoma.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.gogoma.ui.components.TopBar
import com.example.gogoma.ui.screens.MainScreen
import com.example.gogoma.ui.screens.SplashScreen
import com.example.gogoma.ui.components.BottomBar
import com.example.gogoma.ui.components.TopBarArrow
import com.example.gogoma.ui.screens.MypageScreen
import com.example.gogoma.ui.screens.RegistListScreen

@Composable
fun AppNavigationMing() {
    val navController = rememberNavController()
    val currentBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = currentBackStackEntry?.destination?.route

    val currentRouteState = rememberUpdatedState(currentRoute)

    LaunchedEffect (currentRoute) {
        println("Current Route: $currentRoute")
    }

    Scaffold(
        topBar = {
            when (currentRouteState.value) {
                "main" -> TopBar()
                "registList" -> TopBarArrow (
                    title = "신청 내역",
                    onBackClick = { navController.popBackStack() }
                )
            }
        },
        bottomBar = {
            if (currentRoute == "main" || currentRoute == "mypage" || currentRoute == "registList") {
                BottomBar(navController = navController)
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "splash",
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            composable("splash") {
                SplashScreen(navController = navController)
            }
            composable("main") {
                MainScreen(navController)
            }
            composable("mypage") {
                MypageScreen(navController)
            }
            composable("registList") {
                RegistListScreen(navController)
            }
        }
    }
}

@Preview
@Composable
fun TopBarArrowPreview() {
    TopBarArrow(title = "신청 내역", onBackClick = {})
}