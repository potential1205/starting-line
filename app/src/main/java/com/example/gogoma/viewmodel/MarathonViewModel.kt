package com.example.gogoma.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gogoma.GlobalApplication
import com.example.gogoma.data.util.MarathonRealTimeDataUtil
import com.example.gogoma.data.util.MarathonRunService
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.util.Timer
import kotlin.concurrent.fixedRateTimer

class MarathonViewModel(application: Application) : AndroidViewModel(application),
    DataClient.OnDataChangedListener {

    private var marathonStartTimer: Timer? = null
    private var db = GlobalApplication.instance.database
    private val dataClient: DataClient = Wearable.getDataClient(application)
    private var marathonRealTimeDataUtil: MarathonRealTimeDataUtil = MarathonRealTimeDataUtil(getApplication())

    init {
        dataClient.addListener(this)
    }

    override fun onCleared() {
        dataClient.removeListener(this)
        super.onCleared()
    }

    // -------------------------------------------------------------- //
    // -----------------------[Marathon Ready]----------------------- //
    // -------------------------------------------------------------- //
    @SuppressLint("VisibleForTests")
    fun marathonReady() {
        viewModelScope.launch {
            val myInfo = db.myInfoDao().getMyInfo()
            val marathon = db.marathonDao().getMarathon()
            val friendList = db.friendDao().getAllFriends()

            if (myInfo != null && marathon != null) {
                marathonRealTimeDataUtil.setReadyData(myInfo, marathon, friendList)
                sendMarathonReady()
            }

            Log.d("marathon", "[Marathon Ready] myInfo: $myInfo, marathon: $marathon, friendList: $friendList")
        }
    }

    // -------------------------------------------------------------- //
    // -------------------[Marathon Ready to Watch]------------------ //
    // -------------------------------------------------------------- //
    @SuppressLint("VisibleForTests")
    fun sendMarathonReady() {
        val marathonRealTimeData = marathonRealTimeDataUtil.getMarathonRealTimeData()

        val putDataMapRequest = PutDataMapRequest.create("/ready").apply {
            dataMap.putLong("timestamp", System.currentTimeMillis())
            dataMap.putString("marathonTitle", marathonRealTimeData.marathonTitle)
            dataMap.putInt("totalMemberCount", marathonRealTimeData.totalMemberCount)
        }

        val putDataRequest = putDataMapRequest.asPutDataRequest().setUrgent()

        dataClient.putDataItem(putDataRequest)
            .addOnSuccessListener {
                Log.d("marathon", "[Marathon Ready] 상태 전송 성공")
            }
            .addOnFailureListener { e ->
                Log.e("marathon", "[Marathon Ready] 상태 전송 실패", e)
            }
    }

    // -------------------------------------------------------------------------------- //
    // -----------------------[Marathon Start/End Event Listen]----------------------- //
    // ------------------------------------------------------------------------------ //
    @SuppressLint("VisibleForTests")
    override fun onDataChanged(dataEvents: DataEventBuffer) {
        for (event in dataEvents) {
            if (event.type == DataEvent.TYPE_CHANGED) {
                when (event.dataItem.uri.path) {
                    "/start" -> {
                        viewModelScope.launch {
                            val myInfo = db.myInfoDao().getMyInfo()
                            if (myInfo != null) {
                                val intent = Intent(getApplication(), MarathonRunService::class.java).apply {
                                    putExtra("userId", myInfo.id)
                                }
                                getApplication<Application>().startForegroundService(intent)
                            } else {
                                Log.e("marathon", "myInfo null")
                            }
                        }
                        startMarathonSendData()
                        Log.d("marathon", "[Marathon Start] 워치로부터 마라톤 시작 신호 도착")
                    }
                    "/end" -> {
                        // 서비스 종료
                        val intent = Intent(getApplication(), MarathonRunService::class.java)
                        getApplication<Application>().stopService(intent)

                        marathonRealTimeDataUtil.endUpdating()
                        stopMarathonSendData()
                        Log.d("marathon", "[Marathon End] 워치로부터 마라톤 종료 신호 도착")
                    }
                }
            }
        }
    }

    private fun startMarathonSendData() {
        marathonStartTimer = fixedRateTimer(
            name = "MarathonDataSender",
            daemon = true,
            initialDelay = 0L,
            period = 1000L
        ) {
            marathonRealTimeDataUtil.updateData()
            marathonSendData()
        }
    }

    private fun stopMarathonSendData() {
        marathonStartTimer?.cancel()
        marathonStartTimer = null
    }

    // -------------------------------------------------------------------------------- //
    // ----------------------[Marathon Ing - Send Data To Watch]---------------------- //
    // ------------------------------------------------------------------------------ //
    @SuppressLint("VisibleForTests")
    fun marathonSendData() {
        val marathonRealTimeData = marathonRealTimeDataUtil.getMarathonRealTimeData()
        Log.d("marathon", "[Marathon Ing] 데이터 전송 성공 : $marathonRealTimeData")

        val putDataMapRequest = PutDataMapRequest.create("/update").apply {
            dataMap.putInt("totalMemberCount", marathonRealTimeData.totalMemberCount)
            dataMap.putInt("totalDistance", marathonRealTimeData.totalDistance)
            dataMap.putInt("currentDistance", marathonRealTimeData.currentDistance)
            dataMap.putFloat("currentDistanceRate", marathonRealTimeData.currentDistanceRate)
            dataMap.putInt("targetPace", marathonRealTimeData.targetPace)
            dataMap.putInt("currentPace", marathonRealTimeData.currentPace)
            dataMap.putInt("targetTime", marathonRealTimeData.targetTime)
            dataMap.putInt("currentTime", marathonRealTimeData.currentTime)
            dataMap.putInt("myRank", marathonRealTimeData.myRank)
            dataMap.putString("state", marathonRealTimeData.state)

            val jsonFriendInfoList = Gson().toJson(marathonRealTimeData.friendInfoList)
            dataMap.putString("friendInfoList", jsonFriendInfoList)
        }
        val putDataRequest = putDataMapRequest.asPutDataRequest().setUrgent()

        dataClient.putDataItem(putDataRequest)
            .addOnSuccessListener {
                // 데이터 전송 성공 로그
            }
            .addOnFailureListener { e ->
                // 데이터 전송 실패 로그
            }
    }
}
