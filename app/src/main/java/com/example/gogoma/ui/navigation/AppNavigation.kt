package com.example.gogoma.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gogoma.ui.components.TopBar
import com.example.gogoma.ui.screens.MainScreen
import com.example.gogoma.ui.screens.SplashScreen
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.tooling.preview.Preview
import com.example.gogoma.ui.components.BottomBar
import com.example.gogoma.ui.screens.MypageScreen

@Composable
fun AppNavigation(){
    val navController = rememberNavController()

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
                MainScreen(navController, modifier = Modifier
                    .padding(paddingValues)
                )
            }
        }

        composable("mypage") {
            // 마이페이지 화면
            MypageScreen(navController = navController)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AppNavigationPreview() {
    AppNavigation()
}
