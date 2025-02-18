package com.example.gogoma.presentation.screens

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.WindowManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.MaterialTheme
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import androidx.wear.tooling.preview.devices.WearDevices
import com.example.gogoma.presentation.viewmodel.MarathonDataViewModel

@Composable
fun StartScreen(navController: NavController, marathonDataViewModel: MarathonDataViewModel) {
    val context = LocalContext.current
    val activity = context as? Activity
//    val marathonDataViewModel: MarathonDataViewModel = viewModel()

    // ViewModel에서 상태를 가져오기
    val marathonState = marathonDataViewModel.marathonState.collectAsState().value

    // 한 번만 Data Listener를 시작하도록 LaunchedEffect 사용
    LaunchedEffect(Unit) {
        marathonDataViewModel.startDataListener(context)
    }

    KeepScreenOn(activity)
    CheckWearOSConnection()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (marathonState.marathonTitle.isNotEmpty()) {
                Text(
                    text = marathonState.marathonTitle,
                    fontSize = 14.sp,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = {
                        sendStartSignalToPhone(context)
                        navController.navigate("viewPagerScreen")
                    },
                    modifier = Modifier.size(80.dp)
                ) {
                    Text("시작", fontSize = 18.sp, color = Color.Black)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    "참여 ${marathonState.totalMemberCount}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            } else {
                Text("가까운 대회가 없습니다", fontSize = 14.sp, color = Color.White)
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
//    StartScreen(navController = rememberNavController())
}
