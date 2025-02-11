package com.ssafy.gogomawatch.presentation.screens

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable

@Composable
fun StartScreen(navController: NavController) {
    val context = LocalContext.current
    val activity = context as? Activity
    val isAutoSending = remember { mutableStateOf(false) }
    val dataState = remember { mutableStateOf("Waiting for data object...") }

    DisposableEffect(Unit) {
        val dataClient = Wearable.getDataClient(context)
        val listener = DataClient.OnDataChangedListener { dataEvents ->
            dataEvents.forEach { event ->
                if (event.type == DataEvent.TYPE_CHANGED && event.dataItem.uri.path == "/update") {
                    val dataMapItem = DataMapItem.fromDataItem(event.dataItem)
                    val age = dataMapItem.dataMap.getInt("age")
                    val name = dataMapItem.dataMap.getString("name")
                    val timestamp = dataMapItem.dataMap.getLong("timestamp")
                    dataState.value = "Received: name=$name, age=$age, time=$timestamp"
                    Log.d("StartScreen", dataState.value)
                }
            }
        }
        dataClient.addListener(listener)
        onDispose { dataClient.removeListener(listener) }
    }

    KeepScreenOn(activity)

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
            Text("제60회 광주일보 마라톤 대회", fontSize = 12.sp, color = Color.White)
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = {
                    sendStartSignalToPhone(context)
                    isAutoSending.value = true
                    navController.navigate("viewPagerScreen")
                }
            )
            {
                Text("시작!")
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text("친구 3명 참가 중", fontSize = 12.sp, color = Color.Gray)
        }
    }
}

@Composable
fun KeepScreenOn(activity: Activity?) {
    DisposableEffect(activity) {
        activity?.window?.addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        onDispose {
            activity?.window?.clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }
}

private fun sendStartSignalToPhone(context: android.content.Context) {
    val putDataMapRequest = PutDataMapRequest.create("/start").apply {
        dataMap.putBoolean("connected", true)
        dataMap.putLong("time", System.currentTimeMillis())
    }

    val putDataRequest = putDataMapRequest.asPutDataRequest().setUrgent()

    Wearable.getDataClient(context).putDataItem(putDataRequest)
        .addOnSuccessListener { Log.d("StartScreen", "[워치 to 모바일] Marathon Start 요청 성공") }
        .addOnFailureListener { e -> Log.e("StartScreen", "[워치 to 모바일] Marathon Start 요청 실패", e)}
}

@Preview(device = WearDevices.SMALL_ROUND, showSystemUi = true)
@Composable
fun StartScreenPreview() {
    StartScreen(navController = rememberNavController())
}