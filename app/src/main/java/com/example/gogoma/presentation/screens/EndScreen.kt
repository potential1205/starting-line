package com.example.gogoma.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.tooling.preview.devices.WearDevices
import com.example.gogoma.presentation.theme.GogomaWatchTheme
import com.example.gogoma.presentation.viewmodel.MarathonDataViewModel

@Composable
fun EndScreen(marathonDataViewModel: MarathonDataViewModel, navController: NavController) {

    val marathonState = marathonDataViewModel.marathonState.collectAsState().value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp)
            .background(MaterialTheme.colors.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(15.dp),
            modifier = Modifier.fillMaxSize()
                .wrapContentSize(Alignment.Center)
        ) {
            // 등수 및 참가자 수 표시
            Text(
                text = "${marathonState.myRank} / ${marathonState.totalMemberCount} 등",
                style = MaterialTheme.typography.title2,
                textAlign = TextAlign.Center
            )

            // 중간 정보 표시
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "평균 페이스: ${formatPace(marathonState.currentPace)} /km",
                    style = MaterialTheme.typography.body2,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "달린 시간: ${formatTime(marathonState.currentTime)}",
                    style = MaterialTheme.typography.body2,
                    textAlign = TextAlign.Center
                )
            }

            // 종료 버튼
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        marathonDataViewModel.setReturningFromEndScreen(true)
                        navController.navigate("startScreen") {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    modifier = Modifier.size(50.dp)
                ) {
                    Text(
                        "종료",
                        fontSize = 20.sp,
                        style = MaterialTheme.typography.caption1,
                        color = MaterialTheme.colors.background
                    )
                }
            }
        }
    }
}

@Preview(device = WearDevices.SMALL_ROUND, showSystemUi = true)
@Composable
fun EndScreenPreview() {
    GogomaWatchTheme {
        EndScreen(MarathonDataViewModel(), navController = rememberNavController())
    }
}
