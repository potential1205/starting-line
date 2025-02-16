package com.example.gogoma.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gogoma.ui.components.BottomBar
import com.example.gogoma.ui.components.Filter
import com.example.gogoma.ui.components.MarathonListItem
import com.example.gogoma.ui.components.TopBar
import com.example.gogoma.viewmodel.BottomSheetViewModel
import com.example.gogoma.viewmodel.MarathonListViewModel
import com.example.gogoma.viewmodel.ScrollViewModel
import com.example.gogoma.viewmodel.UserViewModel

@Composable
fun MainScreen(
    navController: NavController,
    userViewModel: UserViewModel,
    marathonListViewModel: MarathonListViewModel,
    bottomSheetViewModel: BottomSheetViewModel,
    scrollViewModel: ScrollViewModel,
    onFilterClick: (String) -> Unit,
    onMarathonClick: (Int) -> Unit,
) {
    val marathonList = marathonListViewModel.marathonSearchResponseList
    val isLoading = marathonListViewModel.isLoading
    val errorMessage = marathonListViewModel.errorMessage
    val listState = rememberLazyListState()

    // 스크롤 위치 복원
    LaunchedEffect(scrollViewModel.scrollPosition) {
        listState.scrollToItem(scrollViewModel.scrollPosition.toInt())
    }

    // LazyColumn에서 스크롤 상태가 변경될 때마다 ViewModel에 저장
    LaunchedEffect(listState.firstVisibleItemIndex) {
        scrollViewModel.scrollPosition = listState.firstVisibleItemIndex.toFloat()
    }

    // 뒤로 가기 동작 정의
    BackHandler(enabled = bottomSheetViewModel.isBottomSheetVisible) {
        // 모달창이 열려 있을 때 뒤로 가기 버튼 처리
        if (bottomSheetViewModel.isSubPageVisible) {
            // 모달 내에서 페이지가 바뀌었으면 이전 페이지로 돌아가게 처리
            bottomSheetViewModel.goBackToPreviousPage()
        } else {
            // 처음 연 모달 창이라면 모달 닫기
            bottomSheetViewModel.hideBottomSheet()
        }
    }

    Scaffold (
        topBar = { TopBar() },
        bottomBar = { BottomBar(navController = navController, userViewModel) }
    ){ paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)){

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

            if(isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            } else {
                // 리스트 영역 (스크롤)
                LazyColumn(
                    state = listState,
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
}
