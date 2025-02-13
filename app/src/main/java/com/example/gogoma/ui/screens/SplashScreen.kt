package com.example.gogoma.ui.screens

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.gogoma.MainActivity
import com.example.gogoma.R
import com.example.gogoma.theme.SplashTheme
import com.example.gogoma.viewmodel.UserViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(context: Context) {
    val alpha = remember {
        Animatable(0f)
    }
    LaunchedEffect(Unit) {
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(500)
        )
        delay(2000L)

        Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }.let { intent ->
            context.startActivity(intent)
        }
    }

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
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    val context = LocalContext.current
    SplashScreen(context)
}
