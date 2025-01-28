package com.example.gogoma.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.gogoma.theme.GogomaTheme

@Composable
fun MainScreen(navController: NavController) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Text("이곳은 메인입니다")
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    GogomaTheme {
        MainScreen(navController= rememberNavController())
    }
}