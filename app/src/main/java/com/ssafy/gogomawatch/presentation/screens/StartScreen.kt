package com.ssafy.gogomawatch.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.MaterialTheme
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import androidx.wear.tooling.preview.devices.WearDevices

@Composable
fun StartScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("제60회 광주일보 마라톤 대회", fontSize = 12.sp, color = Color.White)
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = { navController.navigate("personalScreen") }) {
                Text("시작!")
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text("친구 3명 참가 중", fontSize = 12.sp, color = Color.Gray)
        }
    }
}

@Preview(device = WearDevices.SMALL_ROUND, showSystemUi = true)
@Composable
fun StartScreenPreview() {
    StartScreen(navController = rememberNavController())
}