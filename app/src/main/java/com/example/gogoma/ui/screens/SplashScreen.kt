package com.example.gogoma.ui.screens

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.gogoma.R
import com.example.gogoma.theme.SplashTheme

@Composable
fun SplashScreen(navController: NavController) {
    SplashTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.surface,
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize(),
            ) {
                Column(
                    modifier = Modifier.padding(bottom = 20.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.logo_image), // 벡터 파일 로드
                        contentDescription = "Logo image",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(50.dp) // 로고 크기 설정
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.logo), // 벡터 파일 로드
                        contentDescription = "Logo",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(50.dp) // 로고 크기 설정
                    )
                }
                // 3초 후 MainScreen으로 이동
                LaunchedEffect(Unit) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        navController.navigate("main") {
                            popUpTo("splash") { inclusive = true } // 스플래시 화면은 스택에서 제거
                        }
                    }, 2500) // 2.5초 후에 MainScreen으로 이동
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen(navController = rememberNavController())
}
