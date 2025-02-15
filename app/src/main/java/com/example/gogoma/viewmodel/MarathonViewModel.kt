package com.example.gogoma.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gogoma.data.api.RetrofitInstance
import com.example.gogoma.data.model.MarathonStartInitDataResponse
import com.example.gogoma.data.util.MarathonRealTimeDataUtil
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MarathonViewModel(application: Application) : AndroidViewModel(application),
    DataClient.OnDataChangedListener {

    // 외부에서 읽기 전용 상태 변수
    var isMarathonStart by mutableStateOf(false)
        private set
    var isMarathonReady by mutableStateOf(false)
        private set
    var marathonReadyData by mutableStateOf<MarathonStartInitDataResponse?>(null)
        private set

    private var marathonRealTimeDataUtil: MarathonRealTimeDataUtil = MarathonRealTimeDataUtil()
    private val dataClient: DataClient = Wearable.getDataClient(application)

    companion object {
        private const val DUMMY_TOKEN = "nJnX_sMUwtAEGumqrmoYbTD4eDjz0AE1AAAAAQo9dGkAAAGVCMHIp5CBbdpZdq0Z"
        private const val MARATHON_ID = 30
    }

    init {
        dataClient.addListener(this)
    }

    override fun onCleared() {
        dataClient.removeListener(this)
        super.onCleared()
    }

    /**
     * 마라톤 준비 초기 데이터를 API를 통해 받아오고, 워치에 준비 상태를 전송합니다.
     */
    @SuppressLint("VisibleForTests")
    fun marathonReady() {
        viewModelScope.launch {
            try {
                // suspend 함수 호출 (코루틴 내에서 네트워크 요청 진행)
                val response = RetrofitInstance.watchApiService.getMarathonStartInitData(
                    DUMMY_TOKEN,
                    MARATHON_ID
                )
                if (response.isSuccessful) {
                    response.body()?.let { readyData ->
                        marathonReadyData = readyData
                        isMarathonReady = true
                        Log.e("marathon", "Marathon Ready 응답이 성공했습니다 $readyData")
                        marathonRealTimeDataUtil.setReadyData(readyData)
                    } ?: run {
                        Log.e("marathon", "Marathon Ready 응답 본문이 null입니다.")
                    }
                } else {
                    Log.e("marathon", "Marathon Ready 데이터 요청 실패: ${response.errorBody()?.string()}")
                }
            } catch (e: HttpException) {
                Log.e("marathon", "Marathon Ready 데이터 호출 실패: ${e.message}", e)
            } catch (e: Exception) {
                Log.e("marathon", "Unexpected error in Marathon Ready", e)
            } finally {
                sendMarathonReady()
            }
        }
    }

    /**
     * 워치에 마라톤 준비 상태를 전송합니다.
     */
    @SuppressLint("VisibleForTests")
    fun sendMarathonReady() {
        val putDataMapRequest = PutDataMapRequest.create("/ready").apply {
            dataMap.putLong("timestamp", System.currentTimeMillis())
        }
        val putDataRequest = putDataMapRequest.asPutDataRequest().setUrgent()

        dataClient.putDataItem(putDataRequest)
            .addOnSuccessListener {
                Log.d("marathon", "[mobile -> watch] Marathon Ready 상태 전송 성공")
            }
            .addOnFailureListener { e ->
                Log.e("marathon", "[mobile -> watch] Marathon Ready 상태 전송 실패", e)
            }
    }

    /**
     * 워치로부터의 데이터 이벤트를 수신합니다.
     */
    @SuppressLint("VisibleForTests")
    override fun onDataChanged(dataEvents: DataEventBuffer) {
        for (event in dataEvents) {
            if (event.type == DataEvent.TYPE_CHANGED) {
                when (event.dataItem.uri.path) {
                    "/start" -> { // 마라톤 시작 이벤트 수신
                        isMarathonStart = true
                        marathonRealTimeDataUtil?.startUpdating()
                        Log.d("marathon", "워치로부터 마라톤 시작 신호 도착")
                    }
                    "/end" -> { // 마라톤 종료 이벤트 수신
                        isMarathonStart = false
                        marathonRealTimeDataUtil?.endUpdating()
                        Log.d("marathon", "워치로부터 마라톤 종료 신호 도착")
                    }
                }
            }
        }
    }

    /**
     * 마라톤 진행 데이터를 워치에 전송합니다.
     */
    @SuppressLint("VisibleForTests")
    fun marathonStart() {
        val marathonRealTimeData = marathonRealTimeDataUtil?.getMarathonRealTimeData()
        val putDataMapRequest = PutDataMapRequest.create("/update").apply {
            marathonRealTimeData?.let {
                dataMap.putInt("myRank", it.myRank)
            }
            dataMap.putLong("timestamp", System.currentTimeMillis())
        }
        val putDataRequest = putDataMapRequest.asPutDataRequest().setUrgent()

        dataClient.putDataItem(putDataRequest)
            .addOnSuccessListener {
                Log.d("marathon", "워치에게 마라톤 데이터 전송 성공")
            }
            .addOnFailureListener { e ->
                Log.e("marathon", "워치에게 마라톤 데이터 전송 실패", e)
            }
    }
}
