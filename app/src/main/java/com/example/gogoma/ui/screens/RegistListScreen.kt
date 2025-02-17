package com.example.gogoma.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.gogoma.ui.components.BottomBar
import com.example.gogoma.ui.components.RegistListItem
import com.example.gogoma.ui.components.RegistMarathonCountSection
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

    val context = LocalContext.current

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
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                // 마라톤 카운트 표시
                RegistMarathonCountSection(registList)

                // 신청 내역 리스트
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(registList) { regist ->
                        RegistListItem(regist, onClick = {
                            navController.navigate("registDetail/${regist.userMarathonId}")
                        })
                    }
                }
            }
        }

    }
}
