package com.example.gogoma.ui.screens

import android.content.Context
import android.content.Intent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gogoma.MainActivity
import com.example.gogoma.R
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
            Image(
                painter = painterResource(id = R.drawable.logo_road),
                contentDescription = "brand name start line image",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .width(192.dp)
                    .height(323.dp)
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 21.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "logo image",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )
            Text(
                text = "출발선에 서는 순간,\n모든 준비는 끝났다.",
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 28.8.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onPrimary,
                    letterSpacing = -0.64.sp
                ),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 187.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    val context = LocalContext.current
    SplashScreen(context)
}
