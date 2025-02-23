package com.example.gogoma.presentation.screens

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.WindowManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.MaterialTheme
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import androidx.wear.tooling.preview.devices.WearDevices
import com.example.gogoma.presentation.theme.GogomaWatchTheme
import com.example.gogoma.presentation.viewmodel.MarathonDataViewModel

@Composable
fun StartScreen(navController: NavController, marathonDataViewModel: MarathonDataViewModel) {
    val context = LocalContext.current
    val activity = context as? Activity

    // ViewModel에서 상태를 가져오기
    val marathonState = marathonDataViewModel.marathonState.collectAsState().value

    // 한 번만 Data Listener를 시작하도록 LaunchedEffect 사용
    LaunchedEffect(Unit) {
        marathonDataViewModel.startDataListener(context, navController)
    }

    KeepScreenOn(activity)
    CheckWearOSConnection()

    val isReturningFromEnd by marathonDataViewModel.isReturningFromEnd.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize() // 원형 워치 화면 크기
            .clip(CircleShape) // 원형 UI 적용
            .background(MaterialTheme.colors.background)
            .padding(vertical = 10.dp, horizontal = 20.dp), // 원형 내부에서 안전한 영역 확보
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (!isReturningFromEnd && marathonState.marathonTitle.isNotEmpty()) {
                Text(
                    text = marathonState.marathonTitle,
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onBackground,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    softWrap = true, // 자동 줄바꿈 활성화
                    maxLines = 2, // 두 줄까지만 표시
                    modifier = Modifier
                        .width(150.dp) // 작은 화면에 맞춰서 너비 조정
                        .wrapContentHeight() // 내용 크기만큼 높이 설정
                        .paddingFromBaseline(top = 10.dp) // 텍스트가 상단에서 잘리지 않도록 여백 추가
                )
                Spacer(modifier = Modifier.height(5.dp))
                Button(
                    onClick = {
                        sendStartSignalToPhone(context)
                        marathonDataViewModel.startTimer()
                        navController.navigate("viewPagerScreen") {
                            popUpTo("startScreen") { inclusive = true }
                        }
                    },
                    modifier = Modifier.size(80.dp)
                ) {
                    Text(
                        "시작",
                        fontSize = 28.sp,
                        color = MaterialTheme.colors.background,
                        fontFamily = MaterialTheme.typography.caption1.fontFamily
                    )
                }
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    "참여 ${marathonState.totalMemberCount}명",
                    fontSize = 14.sp,
                    color = MaterialTheme.colors.onBackground
                )
            } else {
                Text(
                    text = "이런!",
                    style = MaterialTheme.typography.caption1,
                    color = MaterialTheme.colors.onBackground,
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = "아직 가까운 대회가 없습니다.",
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onBackground,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }

}

@Composable
fun KeepScreenOn(activity: Activity?) {
    DisposableEffect(activity) {
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        onDispose {
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }
}

// 워치에서 모바일로 Start 신호 전송
private fun sendStartSignalToPhone(context: Context) {
    val putDataMapRequest = com.google.android.gms.wearable.PutDataMapRequest.create("/start").apply {
        dataMap.putLong("timestamp", System.currentTimeMillis())
        dataMap.putString("priority", "urgent")
    }

    val putDataRequest = putDataMapRequest.asPutDataRequest().setUrgent()

    com.google.android.gms.wearable.Wearable.getDataClient(context).putDataItem(putDataRequest)
        .addOnSuccessListener {
            Log.d("StartScreen", "[워치 to 모바일] 마라톤 시작 요청 성공")
        }
        .addOnFailureListener { e ->
            Log.e("StartScreen", "[워치 to 모바일] 마라톤 시작 요청 실패", e)
        }
}

@Composable
fun CheckWearOSConnection() {
    val context = LocalContext.current
    DisposableEffect(Unit) {
        val nodeClient = com.google.android.gms.wearable.Wearable.getNodeClient(context)
        nodeClient.connectedNodes
            .addOnSuccessListener { nodes ->
                if (nodes.isNotEmpty()) {
                    Log.d("WearOS", "📡 연결된 모바일 기기: ${nodes.map { it.displayName }}")
                } else {
                    Log.e("WearOS", "❌ 연결된 모바일 기기가 없습니다. Data Layer 이벤트를 받을 수 없습니다!")
                }
            }
            .addOnFailureListener { e ->
                Log.e("WearOS", "⚠️ 모바일 기기 확인 중 오류 발생", e)
            }
        onDispose { }
    }
}

@Preview(device = WearDevices.SMALL_ROUND, showSystemUi = true)
@Composable
fun StartScreenPreview() {
    GogomaWatchTheme {
        StartScreen(navController = rememberNavController(), MarathonDataViewModel())
    }
}
