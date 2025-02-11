package com.example.myapplication.presentation

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Text
import com.example.myapplication.dto.MyData
import com.google.android.gms.wearable.DataClient.OnDataChangedListener
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import android.view.WindowManager
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.platform.LocalContext

class MainActivity : ComponentActivity(), OnDataChangedListener {

    private var dataState = mutableStateOf("Waiting for data object...")
    private val isAutoSending = mutableStateOf(false)
    private val isMarathonReady = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Wearable.getDataClient(this).addListener(this)
            KeepScreenOn()
            WatchScreen(
                message = dataState.value,
                onMarathonStart = { marathonStart() },
                onMarathonEnd = { marathonEnd()},
                isAutoSending = isAutoSending,
                isMarathonReady = isMarathonReady
            )
        }
    }

    // --------------- [마라톤 start 신호 보내기 / to 모바일] ---------------
    private fun marathonStart() {
        isAutoSending.value = true;

        val putDataMapRequest = PutDataMapRequest.create("/start").apply {
            dataMap.putBoolean("connected", true)
            dataMap.putLong("time", System.currentTimeMillis())
        }

        val putDataRequest = putDataMapRequest.asPutDataRequest().setUrgent()

        Wearable.getDataClient(this).putDataItem(putDataRequest)
            .addOnSuccessListener { dataItem ->
                Log.d("leejaehoon", "[워치 to 모바일] Marathon Start 요청 성공")
            }
            .addOnFailureListener { e ->
                Log.e("leejaehoon", "[워치 to 모바일] Marathon Start 요청 실패 ㅠㅠ", e)
            }
    }

    // --------------- [마라톤 end 신호 보내기 / to 모바일] ---------------
    private fun marathonEnd() {
        isAutoSending.value = false;

        val putDataMapRequest = PutDataMapRequest.create("/end").apply {
            dataMap.putBoolean("connected", false)
            dataMap.putLong("time", System.currentTimeMillis())
        }

        val putDataRequest = putDataMapRequest.asPutDataRequest().setUrgent()

        Wearable.getDataClient(this).putDataItem(putDataRequest)
            .addOnSuccessListener { dataItem ->
                Log.d("leejaehoon", "[워치 to 모바일] Marathon End 요청 성공")
            }
            .addOnFailureListener { e ->
                Log.e("leejaehoon", "[워치 to 모바일] Marathon End 요청 실패 ㅠㅠ", e)
            }
    }

    // --------------- [데이터 수신 / from 모바일] ---------------
    override fun onDataChanged(dataEvents: DataEventBuffer) {
        for (event in dataEvents) {
            if (event.type == DataEvent.TYPE_CHANGED) {
                val dataItem = event.dataItem

                if (dataItem.uri.path == "/update") {
                    val dataMapItem = DataMapItem.fromDataItem(dataItem)
                    val age = dataMapItem.dataMap.getInt("age")
                    val name = dataMapItem.dataMap.getString("name")
                    val timestamp = dataMapItem.dataMap.getLong("timestamp")
                    runOnUiThread {
                        dataState.value =
                            "name: ${name}, age: ${age}, time: ${timestamp}"
                    }
                } else if (dataItem.uri.path == "/ready") {
                    val dataMapItem = DataMapItem.fromDataItem(dataItem)
                    val timestamp = dataMapItem.dataMap.getLong("timestamp")
                    isMarathonReady.value = true;
                }
            }
        }
    }

    override fun onDestroy() {
        Wearable.getDataClient(this).removeListener(this)
        super.onDestroy()
    }
}

@SuppressLint("ContextCastToActivity")
@Composable
fun KeepScreenOn() {
    val activity = LocalContext.current as? Activity
    DisposableEffect(activity) {
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        onDispose {
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }
}

@Composable
fun WatchScreen(
    message: String,
    onMarathonStart: () -> Unit,
    onMarathonEnd: () -> Unit,
    isAutoSending: MutableState<Boolean>,
    isMarathonReady: MutableState<Boolean>) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        BasicText(text = message)
        Spacer(modifier = Modifier.height(16.dp))

        if (isMarathonReady.value) {
            if (!isAutoSending.value) {
                Button(onClick = onMarathonStart) {
                    Text(text = "start")
                }
            }

            if (isAutoSending.value) {
                Button(onClick = onMarathonEnd) {
                    Text(text = "stop")
                }
            }
        }

    }
}