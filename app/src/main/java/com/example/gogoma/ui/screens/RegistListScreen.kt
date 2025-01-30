package com.example.gogoma.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.gogoma.ui.components.BottomBar
import com.example.gogoma.ui.components.Regist
import com.example.gogoma.ui.components.RegistListItem
import com.example.gogoma.ui.components.RegistMarathonCountSection
import com.example.gogoma.ui.components.TopBarArrow

@Composable
fun RegistListScreen(navController: NavController) {
    val registList = listOf(
        Regist("24.06.18", "2025 서울마라톤", "2025.03.16", "10km"),
        Regist("24.06.10", "2025 부산마라톤", "2025.04.10", "15km"),
        Regist("24.05.30", "2025 대구마라톤", "2025.05.05", "5km"),
        Regist("24.05.25", "2025 인천마라톤", "2025.06.01", "20km"),
        Regist("24.05.10", "2025 광주마라톤", "2025.07.10", "42km"),
        Regist("24.04.28", "2025 대전마라톤", "2025.08.15", "21km"),
        Regist("23.12.18", "2024 서울마라톤", "2024.03.16", "10km"),
        Regist("23.11.10", "2024 부산마라톤", "2024.04.10", "15km"),
        Regist("23.10.25", "2024 대구마라톤", "2024.05.05", "5km"),
        Regist("23.10.15", "2024 인천마라톤", "2024.06.01", "20km"),
        Regist("23.09.05", "2024 광주마라톤", "2024.07.10", "42km"),
        Regist("23.08.01", "2024 대전마라톤", "2024.08.15", "21km")
    )

    Scaffold(
        topBar = { TopBarArrow(title = "신청 내역") { navController.popBackStack() } },
        bottomBar = { BottomBar(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
        ) {
            // 마라톤 카운트 표시
            RegistMarathonCountSection(registList)

            // 신청 내역 리스트
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(registList) { regist ->
                    RegistListItem(regist)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegistListScreenPreivew() {
    val navController = rememberNavController()
    RegistListScreen(navController)
}
