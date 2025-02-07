package com.example.gogoma

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import com.example.gogoma.theme.GogomaTheme
import com.example.gogoma.ui.navigation.AppNavigation
import com.example.gogoma.viewmodel.UserViewModel

class MainActivity : ComponentActivity() {
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GogomaApp(userViewModel)
        }
    }
}

@Composable
fun GogomaApp(userViewModel: UserViewModel){
    GogomaTheme {
        AppNavigation(userViewModel)
    }
}
