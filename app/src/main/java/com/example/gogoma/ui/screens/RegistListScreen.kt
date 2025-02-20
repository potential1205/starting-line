package com.example.gogoma.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gogoma.ui.components.BottomBar
import com.example.gogoma.ui.components.RegistListItem
import com.example.gogoma.ui.components.TopBarArrow
import com.example.gogoma.utils.TokenManager
import com.example.gogoma.viewmodel.RegistViewModel
import com.example.gogoma.viewmodel.UserViewModel

@Composable
fun RegistListScreen(
    navController: NavController,
    userViewModel: UserViewModel,
    registViewModel: RegistViewModel,
    onRegistClick: (Int) -> Unit
) {
    val registList = registViewModel.registList.collectAsState().value

    val beforeStartList by registViewModel.beforeStartList.collectAsState()
    val afterFinishList by registViewModel.afterFinishList.collectAsState()

    val context = LocalContext.current

    // 현재 선택된 탭 (0: 출발 전, 1: 도착 후)
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabTitles = listOf("예정" to beforeStartList.size, "종료" to afterFinishList.size)

    LaunchedEffect(Unit) {
        val token = TokenManager.getAccessToken(context)
        token?.let { registViewModel.getUserMarathonList(it) }
    }

    Scaffold (
        topBar = {
            TopBarArrow (
                title = "신청 내역",
                onBackClick = { navController.popBackStack() }
            )
        },
        bottomBar = { BottomBar(navController = navController, userViewModel) }
    ){ paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 탭 레이아웃
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(horizontal = 20.dp)
            ) {
                tabTitles.forEachIndexed { index, (title, count) ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index }
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(vertical = 10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = title,
                                fontSize = 14.sp, // ✅ 기본 크기
                                color = if (selectedTabIndex == index) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onSecondary
                            )

                            Spacer(Modifier.height(5.dp))

                            Text(
                                text = count.toString(),
                                fontSize = 18.sp, // ✅ 숫자는 더 크게 표시
                                fontWeight = FontWeight.Bold,
                                color = if (selectedTabIndex == index) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onSecondary
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            // 선택된 탭에 따라 LazyColumn 표시
            if (selectedTabIndex == 0) {
                LazyColumn(
                    modifier = Modifier.padding(horizontal = 10.dp)
                ) {
                    items(beforeStartList) { regist ->
                        RegistListItem(regist, onClick = {
                            navController.navigate("registDetail/${regist.userMarathonId}")
                        })
                    }
                }
            }
            else if (selectedTabIndex == 1) {
                LazyColumn(
                    modifier = Modifier.padding(horizontal = 10.dp)
                ) {
                    items(afterFinishList) { regist ->
                        RegistListItem(regist, onClick = {
                            navController.navigate("registDetail/${regist.userMarathonId}")
                        })
                    }
                }
            }
        }
    }
}
