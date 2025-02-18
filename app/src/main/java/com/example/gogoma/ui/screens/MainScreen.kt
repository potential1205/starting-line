package com.example.gogoma.ui.screens

import android.widget.Space
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.gogoma.R
import com.example.gogoma.ui.components.BottomBar
import com.example.gogoma.ui.components.Filter
import com.example.gogoma.ui.components.MarathonListItem
import com.example.gogoma.ui.components.TopBar
import com.example.gogoma.viewmodel.BottomSheetViewModel
import com.example.gogoma.viewmodel.MarathonListViewModel
import com.example.gogoma.viewmodel.ScrollViewModel
import com.example.gogoma.viewmodel.UserViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

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
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    //리스트로의 이동 동작
    val coroutineScope = rememberCoroutineScope()

    // LazyColumn의 첫 번째 보이는 아이템 인덱스 추적
    val firstVisibleItemIndex by remember { derivedStateOf { listState.firstVisibleItemIndex } }

    // 필터 고정 여부
    val isFilterFixed = firstVisibleItemIndex > 0

    val topInset = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    LaunchedEffect(Unit) {
        val targetIndex = scrollViewModel.scrollIndex
        val targetOffset = scrollViewModel.scrollOffset

        while (!(listState.firstVisibleItemIndex == targetIndex &&
                    listState.firstVisibleItemScrollOffset == targetOffset)) {
            listState.scrollToItem(targetIndex, targetOffset)
            delay(50)  // 50ms 동안 스크롤이 되는 것을 기다림
        }
    }



    LaunchedEffect(listState.firstVisibleItemIndex, listState.firstVisibleItemScrollOffset) {
        // visibleItemsInfo가 존재할 때만 갱신
        if (listState.layoutInfo.visibleItemsInfo.isNotEmpty()) {
            scrollViewModel.scrollIndex = listState.firstVisibleItemIndex
            scrollViewModel.scrollOffset = listState.firstVisibleItemScrollOffset
        }
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
        topBar = {
            Column {
                TopBar(navController)
                if(isFilterFixed) {
                    Filter(
                        onFilterClick = onFilterClick,
                        selectedFilters = marathonListViewModel.selectedFilters
                    )
                }
            }
         },
        bottomBar = { BottomBar(navController = navController, userViewModel) }
    ){ paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = paddingValues.calculateTopPadding(), bottom = paddingValues.calculateBottomPadding()),
            contentAlignment = Alignment.Center
        ){

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
                        .size(60.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            } else {
                // 리스트 영역 (스크롤)
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    //표지 화면
                    item {
                        Column (
                            modifier = Modifier
                                .fillMaxSize()
                                .height(screenHeight - 65.dp)
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.CenterVertically),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ){
                            Column(
                                modifier = Modifier
                                    .weight(1f),
                                verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.icon_steps),
                                    contentDescription = "step icon",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(125.dp)
                                )
                                Text(
                                    text = buildAnnotatedString {
                                        withStyle(style = SpanStyle(fontSize = 29.5.sp)) {
                                            append("첫 번째 ")
                                        }
                                        withStyle(style = SpanStyle(fontSize = 29.5.sp, color = MaterialTheme.colorScheme.primary)) {
                                            append("출발")
                                        }
                                        withStyle(style = SpanStyle(fontSize = 29.5.sp)) {
                                            append(",")
                                        }
                                        withStyle(style = SpanStyle(fontSize = 17.5.sp)) {
                                            append("\n마라톤을 뛰러 가 볼까요?")
                                        }
                                    },
                                    style = TextStyle(
                                        lineHeight = 33.sp,
                                        textAlign = TextAlign.Center,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .padding(top = 24.dp, bottom = 89.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                IconButton (
                                    onClick = {
                                        coroutineScope.launch {
                                            listState.animateScrollToItem(1) //리스트로 이동
                                        }
                                    }
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.icon_keyboard_arrow_down),
                                        contentDescription = "down arrow",
                                        tint = Color(0xFFB0B0B0),
                                        modifier = Modifier.size(58.dp)
                                    )
                                }
                            }
                        }
                    }

                    // 필터 컴포넌트 (스크롤 될 때 고정)
                    item {
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .padding(0.dp)
                            .heightIn(min = screenHeight - paddingValues.calculateTopPadding() - topInset)
                        ){
                            Column {
                                if(!isFilterFixed) {
                                    Filter(
                                        onFilterClick = onFilterClick,
                                        selectedFilters = marathonListViewModel.selectedFilters
                                    )
                                }
                                marathonList.forEach { marathon ->
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

        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview(){
    MainScreen(navController = rememberNavController(),
        userViewModel = UserViewModel(),
        marathonListViewModel = MarathonListViewModel(),
        bottomSheetViewModel = BottomSheetViewModel(),
        scrollViewModel =ScrollViewModel(),
        onFilterClick = {},
    onMarathonClick = {})
}


