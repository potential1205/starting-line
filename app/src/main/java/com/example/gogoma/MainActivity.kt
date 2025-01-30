package com.example.gogoma

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import com.example.gogoma.theme.GogomaTheme
import com.example.gogoma.ui.navigation.AppNavigation
import com.example.gogoma.ui.navigation.PaymentNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GogomaApp()
        }
    }
}

@Composable
fun GogomaApp(){
    GogomaTheme {
        // AppNavigation()
        PaymentNavigation() // 결제 로직 테스트용
    }
}

