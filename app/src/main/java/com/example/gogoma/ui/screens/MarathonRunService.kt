package com.example.gogoma.ui.screens

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.gogoma.BuildConfig
import com.example.gogoma.data.dto.MarathonReadyDto
import com.example.gogoma.data.dto.MyData
import com.example.gogoma.services.MarathonApiService
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.delay
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MarathonRunService : ComponentActivity(), DataClient.OnDataChangedListener {

    private var serverBaseUrl = "http://${BuildConfig.SERVER_IP}:8080/api/v1/watch/"
    private var receivedState = mutableStateOf("Waiting for data...")
    private var marathonReadyData = mutableStateOf<MarathonReadyDto?>(null)
    private val isAutoSending = mutableStateOf(false)
    private val isMarathonReady = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Wearable.getDataClient(this).addListener(this)

        setContent {
            PhoneScreen(
                marathonReadyData = marathonReadyData.value,
                isAutoSending = isAutoSending,
                onMarathonReady = { marathonReady() },
                onSendData = { sendDataToWearable(MyData("김용현", 100)) },
                isMarathonReady = isMarathonReady,
            )
        }
    }

    // --------------- [초기 데이터 불러오기 / from 서버] ---------------
    @SuppressLint("VisibleForTests")
    private fun marathonReady() {
        val retrofit = Retrofit.Builder()
            .baseUrl(serverBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(MarathonApiService::class.java)

        val userId = 5
        val marathonId = 25
        val token = "kakao access token"

        apiService.startMarathon(marathonId, userId).enqueue(object : Callback<MarathonReadyDto> {
            override fun onResponse(
                call: Call<MarathonReadyDto>,
                response: Response<MarathonReadyDto>
            ) {
                if (response.isSuccessful) {
                    Log.d("print", "Marathon Ready 성공: ${response.body()}")
                    marathonReadyData.value = response.body()
                    isMarathonReady.value = true;
                    sendMarathonReady();
                } else Log.e("print", "Marathon Ready 응답 에러 ${response.errorBody()?.string()}")
            }

            override fun onFailure(call: Call<MarathonReadyDto>, t: Throwable) {
                Log.e("print", "Marathon Ready 호출 실패", t)
            }
        })
    }

    // --------------- [데이터 송수신 이벤트 감지 / from 워치] ---------------
    @SuppressLint("VisibleForTests")
    override fun onDataChanged(dataEvents: DataEventBuffer) {
        for (event in dataEvents) {
            if (event.type == DataEvent.TYPE_CHANGED) {
                val dataItem = event.dataItem

                when (dataItem.uri.path) {
                    "/start" -> { // 워치로 부터 /start 요청이 오면
                        val dataMapItem = DataMapItem.fromDataItem(dataItem)
                        val timestamp = dataMapItem.dataMap.getLong("time")
                        isAutoSending.value = true;
                        runOnUiThread {
                            receivedState.value = "start time : $timestamp"
                        }
                        Log.d("print", "mobile -> watch] Marathon Start 성공: ")
                    }
                    "/end" -> { // 워치로 부터 /end 요청이 오면
                        isAutoSending.value = false
                        runOnUiThread {
                            Log.d("print", "Marathon End 성공: ")
                        }
                    }
                }
            }
        }
    }

    // --------------- [데이터 송신 / to 워치] ---------------
    @SuppressLint("VisibleForTests")
    private fun sendDataToWearable(data: MyData) {
        val putDataMapRequest = PutDataMapRequest.create("/update").apply {
            dataMap.putInt("age", data.age)
            dataMap.putString("name", data.name)
            dataMap.putLong("timestamp", System.currentTimeMillis())
        }

        val putDataRequest = putDataMapRequest.asPutDataRequest().setUrgent()

        Wearable.getDataClient(this).putDataItem(putDataRequest)
            .addOnSuccessListener { dataItem ->
                Log.e("print", "mobile -> watch] Marathon 데이터 전송 성공: $dataItem")
            }
            .addOnFailureListener { e ->
                Log.e("print", "mobile -> watch]Marathon 데이터 전송 실패", e)
            }
    }

    @SuppressLint("VisibleForTests")
    private fun sendMarathonReady() {
        val putDataMapRequest = PutDataMapRequest.create("/ready").apply {
            dataMap.putLong("timestamp", System.currentTimeMillis())
        }

        val putDataRequest = putDataMapRequest.asPutDataRequest().setUrgent()

        Wearable.getDataClient(this).putDataItem(putDataRequest)
            .addOnSuccessListener { dataItem ->
                Log.e("print", "[mobile -> watch] Marathon Ready 전송 성공: $dataItem")
            }
            .addOnFailureListener { e ->
                Log.e("print", "[mobile -> watch] Marathon Ready 전송 실패", e)
            }
    }

    override fun onDestroy() {
        Wearable.getDataClient(this).removeListener(this)
        super.onDestroy()
    }
}

// --------------- [화면] ---------------
@Composable
fun PhoneScreen(
    onSendData: () -> Unit,
    onMarathonReady: () -> Unit,
    marathonReadyData: MarathonReadyDto?,
    isAutoSending: MutableState<Boolean>,
    isMarathonReady: MutableState<Boolean>) {

    LaunchedEffect(isAutoSending.value) {
        while (isAutoSending.value) {
            onSendData()
            delay(1000)
        }
    }

    val currentTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "time: $currentTime\n Marathon Ready Data:\n$marathonReadyData")

        // Marathon Ready Button
        if (!isMarathonReady.value) {
            Button(onClick = { onMarathonReady() }) {
                Text(text = "Ready")
            }
        }
    }
}
