package com.example.gogoma.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.gogoma.ui.components.Filter
import com.example.gogoma.ui.components.MarathonListItem
import com.example.gogoma.viewmodel.MarathonListViewModel

@Composable
fun MainScreen(
    navController: NavController,
    onFilterClick: (String) -> Unit,
    onMarathonClick: (Int) -> Unit,
) {
    val marathonListViewModel: MarathonListViewModel = viewModel()
    val marathonList = marathonListViewModel.marathonSearchResponseList
    val isLoading = marathonListViewModel.isLoading
    val errorMessage = marathonListViewModel.errorMessage

    Box(
        modifier = Modifier
            .fillMaxSize()
    ){

        // 개발용, 추후 삭제 : 오류 메시지가 있을 경우 표시
        errorMessage?.let {
            // 오류 메시지 UI
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Error: $it",
                    color = Color.Red,  // 빨간색으로 오류 메시지 표시
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        //스크롤 영역
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
//            item {
//                SearchBar()
//            }
            item {
                Filter(
                    onFilterClick = onFilterClick
                )
            }
            // 마라톤 리스트
            if (isLoading) {
                // 로딩 중일 때 표시
                item {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        color = Color.Blue
                    )
                }
            } else {
                items(marathonList) { marathon ->
                    MarathonListItem(marathonPreviewDto = marathon, onClick = {
                        navController.navigate("marathonDetail/${marathon.id}")
                    })
                }
            }
        }
    }
}