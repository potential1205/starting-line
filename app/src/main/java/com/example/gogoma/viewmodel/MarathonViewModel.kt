package com.example.gogoma.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gogoma.data.util.UserDistanceRepository
import com.example.gogoma.data.util.MarathonRealTimeDataUtil
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.launch
import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.core.content.ContextCompat.checkSelfPermission
import com.example.gogoma.GlobalApplication
import com.google.android.gms.location.*
import com.google.gson.Gson
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
                Log.d("marathon", "[Marathon Ready] Marathon Ready 상태 전송 성공")
            }
            .addOnFailureListener { e ->
                Log.e("marathon", "[Marathon Ready] Marathon Ready 상태 전송 실패", e)
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
                                startMarathonRun(myInfo.id)
                            } else {
                                Log.e("marathon", "myInfo null")
                            }
                        }
                        startMarathonSendData()
                        Log.d("marathon", "[Marathon Start] 워치로부터 마라톤 시작 신호 도착")
                    }
                    "/end" -> {
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
                //Log.d("marathon", "워치에게 마라톤 데이터 전송 성공")
            }
            .addOnFailureListener { e ->
                //Log.e("marathon", "워치에게 마라톤 데이터 전송 실패", e)
            }
    }

    // 계산 관련 (누적 거리를 cm 단위로 저장)
    private var cumulativeDistance: Int = 0
    private var previousLocation: Location? = null

    // Location 관련 변수
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    // --------------- [Start 버튼 클릭 시 실행되는 로직] ---------------
    @SuppressLint("VisibleForTests")
    private fun startMarathonRun(currentUserId : Int) {

        // 1. 초기 데이터 생성: 누적 거리를 0 (cm 단위)로 DB에 저장
        UserDistanceRepository.createInitialUserData(
            currentUserId,
            onSuccess = { Log.d("MarathonRunService", "초기 데이터 생성 성공") },
            onFailure = { exception ->
                Log.e("MarathonRunService", "초기 데이터 생성 실패: ${exception.message}")
            }
        )
        cumulativeDistance = 0
        previousLocation = null

        // 2. 위치 업데이트 시작: fusedLocationClient 초기화
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplication())
        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000L)
            .setMinUpdateIntervalMillis(1000)
            .build()

        locationCallback = object : LocationCallback() {
            @SuppressLint("DefaultLocale")
            override fun onLocationResult(locationResult: LocationResult) {
                for (currentLocation in locationResult.locations) {
                    Log.d("MarathonRunService", "현재 위치 -> 위도: ${currentLocation.latitude}, 경도: ${currentLocation.longitude}")
                    previousLocation?.let { prevLoc ->
                        // 이전 위치와 현재 위치 간 거리를 구한 후 cm 단위로 변환
                        val distanceInCm = Math.round(prevLoc.distanceTo(currentLocation) * 100)
                        cumulativeDistance += distanceInCm

                        // 화면 표시용: 누적 거리와 이번 이동 거리를 km 단위로 변환 (1km = 100000cm) 후 소수 둘째자리로 포맷
                        val formattedCumulative = String.format("%.2f", cumulativeDistance / 100000.0)
                        val formattedIncrement = String.format("%.2f", distanceInCm / 100000.0)
                        Log.d("MarathonRunService", "이번 이동 거리: $formattedIncrement km, 누적 거리: $formattedCumulative km")

                        // DB 업데이트: 누적 거리를 cm 단위로 업데이트
                        UserDistanceRepository.updateUserCumulativeDistance(
                            currentUserId,
                            cumulativeDistance,
                            onSuccess = {
                                Log.d("MarathonRunService", "Firebase 업데이트 성공: ${cumulativeDistance} cm")
                            },
                            onFailure = { exception ->
                                Log.e("MarathonRunService", "Firebase 업데이트 실패: ${exception.message}")
                            }
                        )
                    }
                    if (previousLocation == null) {
                        Log.d("MarathonRunService", "첫 위치 업데이트입니다.")
                    }
                    previousLocation = currentLocation
                }
            }
        }

        startLocationUpdates()
    }

    private fun startLocationUpdates() {
        if (checkSelfPermission(getApplication(),Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            checkSelfPermission(getApplication(),Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("MarathonRunService", "위치 권한 부족")
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

}
