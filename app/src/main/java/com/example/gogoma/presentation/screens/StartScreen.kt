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
import androidx.navigation.compose.rememberNavController
import androidx.wear.tooling.preview.devices.WearDevices
import com.google.android.gms.wearable.*
import com.example.gogoma.presentation.data.MarathonData
import com.google.gson.Gson

@Composable
fun StartScreen(navController: NavController) {
    val context = LocalContext.current
    val activity = context as? Activity
    val marathonData = remember { mutableStateOf<MarathonData?>(null) }
    val isMarathonReady = remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        val dataClient = Wearable.getDataClient(context)
        val listener = DataClient.OnDataChangedListener { dataEvents ->
            Log.d("StartScreen", "📡 onDataChanged() 호출됨! 데이터 이벤트 감지")

            dataEvents.forEach { event ->
                val dataItem = event.dataItem
                val path = dataItem.uri.path
                Log.d("StartScreen", "📩 데이터 수신: ${dataItem.uri}") // ✅ 로그로 확인

                if (event.type == DataEvent.TYPE_CHANGED) {
                    Log.d("StartScreen", "📥 데이터 변경 감지, path: $path")

                    if (path?.endsWith("/ready") == true) {
                        try {
                            val dataMapItem = DataMapItem.fromDataItem(event.dataItem)
                            val dataMap = dataMapItem.dataMap

                            // 데이터맵에 marathonData 키가 있는지 체크
                            if (!dataMap.containsKey("marathonData")) {
                                Log.e("StartScreen", "❌ 데이터맵에 marathonData 키가 존재하지 않음!")
                            } else {
                                Log.d("StartScreen", "✅ 데이터맵에서 marathonData 키 확인됨.")
                            }

                            val jsonData = dataMap.getString("marathonData")

                            // marathonData 데이터 상태 확인
                            if (jsonData == null) {
                                Log.e("StartScreen", "❌ marathonData is NULL!")
                            } else if (jsonData.isEmpty()) {
                                Log.e("StartScreen", "❌ marathonData is EMPTY!")
                            } else {
                                Log.d("StartScreen", "📦 marathonData 원본 데이터: $jsonData")

                                val receivedData = Gson().fromJson(jsonData, MarathonData::class.java)
                                marathonData.value = receivedData
                                isMarathonReady.value = true
                                Log.d("StartScreen", "✅ 마라톤 준비 완료: $receivedData")
                            }
                        } catch (e: Exception) {
                            Log.e("StartScreen", "❌ 데이터 변환 실패", e)
                        }
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
            if (isMarathonReady.value && marathonData.value != null) {
                val data = marathonData.value!!

                Text(data.time.toString(), fontSize = 14.sp, color = Color.White)
                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { sendStartSignalToPhone(context); navController.navigate("viewPagerScreen") },
                    modifier = Modifier.size(80.dp)
                ) {
                    Text("시작", fontSize = 18.sp, color = Color.Black)
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text("참여 ${data.totalMemberCount}명", fontSize = 12.sp, color = Color.Gray)
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
    StartScreen(navController = rememberNavController())
}