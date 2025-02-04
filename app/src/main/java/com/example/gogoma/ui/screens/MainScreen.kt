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
import androidx.navigation.NavController
import com.example.gogoma.ui.components.Filter
import com.example.gogoma.ui.components.MarathonListItem
import com.example.gogoma.viewmodel.MarathonListViewModel

@Composable
fun MainScreen(
    navController: NavController,
    marathonListViewModel: MarathonListViewModel,
    onFilterClick: (String) -> Unit,
    onMarathonClick: (Int) -> Unit,
) {
    val marathonList = marathonListViewModel.marathonSearchResponseList
    val isLoading = marathonListViewModel.isLoading
    val errorMessage = marathonListViewModel.errorMessage

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        // 오류 메시지가 있을 경우 표시
        errorMessage?.let { msg ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Error: $msg",
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        // 리스트 영역 (스크롤)
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 필터 컴포넌트
            item {
                Filter(
                    onFilterClick = onFilterClick,
                    selectedFilters = marathonListViewModel.selectedFilters
                )
            }
            // 로딩 중일 때
            if (isLoading) {
                item {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        color = Color.Blue
                    )
                }
            } else {
                // 받아온 리스트 데이터 렌더링
                items(marathonList) { marathon ->
                    MarathonListItem(
                        marathonPreviewDto = marathon,
                        onClick = { onMarathonClick(marathon.id) }
                    )
                }
            }
        }
    }
}
