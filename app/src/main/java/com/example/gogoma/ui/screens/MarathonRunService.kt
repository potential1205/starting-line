//package com.example.gogoma.ui.screens
//
//import android.annotation.SuppressLint
//import android.os.Bundle
//import android.util.Log
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.material3.Button
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.MutableState
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.ui.Modifier
//import com.example.gogoma.data.api.RetrofitInstance
//import com.example.gogoma.data.dto.MarathonReadyDto
//import com.example.gogoma.data.util.MarathonRealTimeDataUtil
//import com.google.android.gms.wearable.*
//import kotlinx.coroutines.delay
//import retrofit2.*
//
//class MarathonRunService : ComponentActivity(), DataClient.OnDataChangedListener {
//    private val isMarathonStart = mutableStateOf(false)
//    private val isMarathonReady = mutableStateOf(false)
//    private var marathonReadyData = mutableStateOf<MarathonReadyDto?>(null) // Room DB에 저장해서 써야됨
//
//    private var marathonRealTimeDataUtil: MarathonRealTimeDataUtil? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        Wearable.getDataClient(this).addListener(this)
//
//        setContent {
//            PhoneScreen(
//                onMarathonReady = { marathonReady() },
//                onMarathonStart = { marathonStart() },
//                isMarathonReady = isMarathonReady,
//                isMarathonStart = isMarathonStart,
//            )
//        }
//    }
//
//    // ------------------------------ [Marathon Ready] ------------------------------
//    @SuppressLint("VisibleForTests")
//    fun marathonReady() {
//        val token = "kakao access token" // userId 대신에 써야함
//
//        RetrofitInstance.watchApiService.getTestMarathonStartInitData(25, 5)
//            .enqueue(object : Callback<MarathonReadyDto> {
//                override fun onResponse(
//                    call: Call<MarathonReadyDto>,
//                    response: Response<MarathonReadyDto>
//                ) {
//                    if (response.isSuccessful) {
//                        response.body()?.let { readyData ->
//                            marathonReadyData.value = readyData
//                            isMarathonReady.value = true
//
//                            // 마라톤 준비 데이터 저장 -> roomDB에서 꺼내오도록 변경해야함
//                            marathonRealTimeDataUtil = MarathonRealTimeDataUtil(readyData)
//                        }
//                    } else {
//                        Log.e("marathon", "Marathon Ready 데이터 요청 실패")
//                    }
//                }
//
//                override fun onFailure(call: Call<MarathonReadyDto>, t: Throwable) {
//                    Log.e("marathon", "Marathon Ready 데이터 호출 실패")
//                }
//            })
//
//        sendMarathonReady()
//    }
//
//    // ------------------------------ [Marathon Ready - 상태 전송] ------------------------------
//    @SuppressLint("VisibleForTests")
//    fun sendMarathonReady() {
//        val putDataMapRequest = PutDataMapRequest.create("/ready").apply {
//            dataMap.putLong("timestamp", System.currentTimeMillis())
//        }
//
//        val putDataRequest = putDataMapRequest.asPutDataRequest().setUrgent()
//
//        Wearable.getDataClient(this).putDataItem(putDataRequest)
//            .addOnSuccessListener {
//                Log.e("marathon", "[mobile -> watch] Marathon Ready 상태 전송 성공")
//            }
//            .addOnFailureListener { e ->
//                Log.e("marathon", "[mobile -> watch] Marathon Ready 상태 전송 실패", e)
//            }
//    }
//
//
//    // ------------------------------ [Marathon Start - 이벤트 감지] ------------------------------
//    @SuppressLint("VisibleForTests")
//    override fun onDataChanged(dataEvents: DataEventBuffer) {
//        for (event in dataEvents) {
//            if (event.type == DataEvent.TYPE_CHANGED) {
//                val dataItem = event.dataItem
//
//                when (dataItem.uri.path) {
//                    "/start" -> { // 워치로부터 /start 요청이 오면
//                        isMarathonStart.value = true
//                        marathonRealTimeDataUtil?.startUpdating()
//
//                        Log.e("marathon", "워치로부터 마라톤 시작 신호가 도착했습니다.")
//                    }
//                    "/end" -> { // 워치로부터 /end 요청이 오면
//                        isMarathonStart.value = false
//                        marathonRealTimeDataUtil?.endUpdating()
//
//                        Log.e("marathon", "워치로부터 마라톤 종료 신호가 도착했습니다.")
//                    }
//                }
//            }
//        }
//    }
//
//    // ------------------------------ [Marathon Start - 데이터 전송] ------------------------------
//    @SuppressLint("VisibleForTests")
//    fun marathonStart() {
//        val marathonReadTimeData = marathonRealTimeDataUtil?.getMarathonRealTimeData()
//
//        val putDataMapRequest = PutDataMapRequest.create("/update").apply {
//            if (marathonReadTimeData != null) {
//                dataMap.putInt("myRank", marathonReadTimeData.myRank)
//            }
//            dataMap.putLong("timestamp", System.currentTimeMillis())
//        }
//
//        val putDataRequest = putDataMapRequest.asPutDataRequest().setUrgent()
//
//        Wearable.getDataClient(this).putDataItem(putDataRequest)
//            .addOnSuccessListener {
//                Log.e("marathon", "워치에게 마라톤 데이터 전송을 성공했습니다.")
//            }
//            .addOnFailureListener {
//                Log.e("marathon", "워치에게 마라톤 데이터 전송을 실패했습니다.")
//            }
//    }
//
//    override fun onDestroy() {
//        Wearable.getDataClient(this).removeListener(this)
//        super.onDestroy()
//    }
//}
//
//// ------------------------------ [화면] ------------------------------
//@Composable
//fun PhoneScreen(
//    onMarathonStart: () -> Unit,
//    onMarathonReady: () -> Unit,
//    isMarathonStart: MutableState<Boolean>,
//    isMarathonReady: MutableState<Boolean>,
//) {
//    LaunchedEffect(isMarathonStart.value) {
//        while (isMarathonStart.value) {
//            onMarathonStart()
//            delay(1000)
//        }
//    }
//
//    Column(
//        modifier = Modifier.fillMaxSize()) {
//        if (!isMarathonReady.value) {
//            Button(onClick = { onMarathonReady() }) {
//                Text(text = "Ready")
//            }
//        }
//    }
//}
