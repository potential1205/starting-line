package com.example.gogoma.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.gogoma.ui.components.Regist
import com.example.gogoma.ui.components.RegistListItem
import com.example.gogoma.ui.components.RegistMarathonCountSection
import com.example.gogoma.viewmodel.RegistViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun RegistListScreen(navController: NavController, viewModel: RegistViewModel, onRegistClick: (Regist) -> Unit) {
    val registList by viewModel.registList.collectAsState()

    // 마라톤 날짜 순으로 정렬
    val sortedRegistList = registList.sortedBy {
        SimpleDateFormat("yyyy.MM.dd", Locale.KOREA).parse(it.date)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // 마라톤 카운트 표시
        RegistMarathonCountSection(sortedRegistList)

        // 신청 내역 리스트
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(sortedRegistList) { regist ->
                RegistListItem(regist, onClick = {
//                    navController.navigate("registDetail/${sortedRegistList.indexOf(regist)}")
                })
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegistListScreenPreivew() {
    val navController = rememberNavController()
    val previewViewModel = RegistViewModel().apply {
        addRegist(Regist("24.06.18", "2025 서울마라톤", "2025.03.16", "10km"))
    }
    RegistListScreen(navController, viewModel = previewViewModel, onRegistClick = {})
}
