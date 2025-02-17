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
import com.google.android.gms.wearable.*
import com.example.gogoma.presentation.data.MarathonData
import com.example.gogoma.presentation.viewmodel.MarathonDataViewModel
import com.google.gson.Gson

@Composable
fun StartScreen(navController: NavController) {
    val context = LocalContext.current
    val activity = context as? Activity
    val marathonData = remember { mutableStateOf<MarathonData?>(null) }
    val isMarathonReady = remember { mutableStateOf(false) }
    val gson = Gson()

    var marathonDataViewModel : MarathonDataViewModel = viewModel()

    DisposableEffect(Unit) {
        val dataClient = Wearable.getDataClient(context)
        val listener = DataClient.OnDataChangedListener { dataEvents ->
            Log.d("StartScreen", "📡 onDataChanged() 호출됨! 데이터 이벤트 감지")

            for (event in dataEvents) {
                if (event.type == DataEvent.TYPE_CHANGED) {
                    val dataItem = event.dataItem

                    if (dataItem.uri.path == "/update") {
                        val dataMapItem = DataMapItem.fromDataItem(dataItem)
                        val age = dataMapItem.dataMap.getInt("age")
                        val name = dataMapItem.dataMap.getString("name")
                        val timestamp = dataMapItem.dataMap.getLong("timestamp")

                    } else if (dataItem.uri.path == "/ready") {
                        val dataMapItem = DataMapItem.fromDataItem(dataItem)
                        val timestamp = dataMapItem.dataMap.getLong("timestamp")
                        val totalMemberCount = dataMapItem.dataMap.getInt("totalMemberCount")
                        val marathonTitle = dataMapItem.dataMap.getString("marathonTitle")

                        if (marathonTitle != null) {
                            marathonDataViewModel.updateInitData(totalMemberCount, marathonTitle)
                        }

                        isMarathonReady.value = true;
                    }
                }
            }
        }
        dataClient.addListener(listener)
        Log.d("StartScreen", "📡 Data Layer 이벤트 리스너 추가됨")
        onDispose {
            Log.d("StartScreen", "❌ Data Layer 이벤트 리스너 제거됨")
            dataClient.removeListener(listener)
        }
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
            if (isMarathonReady.value) {

                Text("${marathonDataViewModel._marathonState.value.marathonTitle}명", fontSize = 14.sp, color = Color.White)
                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { sendStartSignalToPhone(context); navController.navigate("viewPagerScreen") },
                    modifier = Modifier.size(80.dp)
                ) {
                    Text("시작", fontSize = 18.sp, color = Color.Black)
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text("참여 ${marathonDataViewModel._marathonState.value.totalMemberCount}", fontSize = 12.sp, color = Color.Gray)
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

// 📌 워치에서 모바일로 Start 신호 전송
private fun sendStartSignalToPhone(context: Context) {
    val putDataMapRequest = PutDataMapRequest.create("/start").apply {
        dataMap.putLong("timestamp", System.currentTimeMillis())
        dataMap.putString("priority", "urgent")
    }

    val putDataRequest = putDataMapRequest.asPutDataRequest().setUrgent()

    Wearable.getDataClient(context).putDataItem(putDataRequest)
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
        val nodeClient = Wearable.getNodeClient(context)

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
    StartScreen(
        navController = rememberNavController()
    )
}