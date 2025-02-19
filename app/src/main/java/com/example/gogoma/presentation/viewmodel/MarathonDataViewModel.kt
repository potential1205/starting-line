package com.example.gogoma.presentation.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.gogoma.presentation.data.FriendInfo
import com.example.gogoma.presentation.data.MarathonData
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.Wearable
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

class MarathonDataViewModel : ViewModel() {

    // 기존 MarathonData 상태
    var _marathonState = MutableStateFlow(
        MarathonData(
            time = System.currentTimeMillis(),
            totalDistance = 0,
            currentDistance = 0,
            currentDistanceRate = 0f,
            targetPace = 0, // 목표 페이스 (초)
            currentPace = 0, // 현재 페이스 (초)
            targetTime = 0,
            currentTime = 0,
            state = "G",
            myRank = 0,
            totalMemberCount = 0,
            friendInfoList = emptyList(),
            marathonTitle = ""
        )
    )
    val marathonState: StateFlow<MarathonData> = _marathonState

    // ✅ "이전 거리 내 인원 수"를 저장하는 변수 추가 (Fragment가 재생성되어도 유지됨)
    private val _nearbyCount = MutableStateFlow(0)
    val nearbyCount: StateFlow<Int> = _nearbyCount

    // 현재 인덱스 상태 추가
    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex

    // 시간 추적 (초 단위)
    private val _elapsedTime = MutableStateFlow(0)
    val elapsedTime: StateFlow<Int> = _elapsedTime

    // 색상 상태 추가 (Compose UI의 Color 사용)
    private val _currentColor = MutableStateFlow(Color.Gray)
    val currentColor: StateFlow<Color> = _currentColor

    // Data Layer 이벤트 리스너 관련 변수
    private var dataClientListener: DataClient.OnDataChangedListener? = null
    private var appContext: Context? = null

    private var timerJob: Job? = null

    init {
        // 1초마다 시간 갱신
//        viewModelScope.launch {
//            while (true) {
//                delay(1000L)
//                updateElapsedTime()
//            }
//        }
        startTimer()
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000L)
                updateElapsedTime()
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        Log.d("MarathonDataViewModel", "타이머 정지")
    }

    // 인덱스 변경 함수
    fun nextStatus(total: Int) {
        _currentIndex.value = (_currentIndex.value + 1) % total
    }

    // 경과 시간 업데이트
    private fun updateElapsedTime() {
        val newTime = _elapsedTime.value + 1
        _elapsedTime.value = newTime
    }

    // 상태에 따라 currentColor 변경
    fun updateMarathonState(updatedState: MarathonData) {
        _marathonState.value = updatedState  // MarathonData 상태 변경

        // 상태에 따라 currentColor 변경
        _currentColor.value = when (updatedState.state) {
            "G" -> Color(0xFF2680FF)
            "Y" -> Color(0xFFD7A800)
            else -> Color(0xFFFF291A)
        }
    }

    // 초기 데이터 업데이트 (예: /ready 이벤트 처리)
    fun updateInitData(totalMemberCount: Int, marathonTitle: String) {
        _marathonState.value = _marathonState.value.copy(
            totalMemberCount = totalMemberCount,
            marathonTitle = marathonTitle
        )
    }

    fun startDataListener(context: Context, navController: NavController) {
        appContext = context.applicationContext
        val dataClient = Wearable.getDataClient(appContext!!)
        val gson = Gson()

        dataClientListener?.let { dataClient.removeListener(it) }

        dataClientListener = DataClient.OnDataChangedListener { dataEvents ->
            Log.d("MarathonDataViewModel", "📡 onDataChanged() 호출됨! 이벤트 수신")
            for (event in dataEvents) {
                if (event.type == DataEvent.TYPE_CHANGED) {
                    val dataItem = event.dataItem
                    when (dataItem.uri.path) {
                        "/update" -> {
                            val dataMapItem = DataMapItem.fromDataItem(dataItem)
                            val dataMap = dataMapItem.dataMap

                            val totalDistance = dataMap.getInt("totalDistance")
                            val currentDistance = dataMap.getInt("currentDistance")
                            val currentDistanceRate = dataMap.getFloat("currentDistanceRate")
                            val targetPace = dataMap.getInt("targetPace")
                            val currentPace = dataMap.getInt("currentPace")
                            val targetTime = dataMap.getInt("targetTime")
                            val currentTime = dataMap.getInt("currentTime")
                            val myRank = dataMap.getInt("myRank")
                            val state = dataMap.getString("state")
                            val totalMemberCount = dataMap.getInt("totalMemberCount")

                            val jsonFriendInfoList = dataMap.getString("friendInfoList")
                            val friendInfoListType = object : TypeToken<List<FriendInfo>>() {}.type
                            val friendInfoList: List<FriendInfo> = gson.fromJson(jsonFriendInfoList, friendInfoListType)
                            val newNearbyCount = friendInfoList.count { !it.isMe && it.gapDistance.absoluteValue <= 10000 }
                            _nearbyCount.value = newNearbyCount

                            Log.d("marathon", "update 이벤트가 발생했습니다.")

                            state?.let {
                                updateMarathonState(
                                    _marathonState.value.copy(
                                        totalDistance = totalDistance,
                                        targetPace = targetPace,
                                        targetTime = targetTime,
                                        currentDistance = currentDistance,
                                        currentDistanceRate = currentDistanceRate,
                                        currentPace = currentPace,
                                        currentTime = currentTime,
                                        myRank = myRank,
                                        totalMemberCount = totalMemberCount,
                                        friendInfoList = friendInfoList,
                                        state = it
                                    )
                                )
                            }

                            // 이제 워치는 end 관련 신호 받으면 화면 전환 처리만
//                            if (currentDistance >= totalDistance) {
//                                navController.navigate("endScreen")
//                            }

                            Log.d("marathon", _marathonState.value.toString())
                        }
                        "/ready" -> {
                            val dataMapItem = DataMapItem.fromDataItem(dataItem)
                            val dataMap = dataMapItem.dataMap
                            val timestamp = dataMap.getLong("timestamp")
                            val totalMemberCount = dataMap.getInt("totalMemberCount")
                            val marathonTitle = dataMap.getString("marathonTitle")
                            Log.d("MarathonDataViewModel", "ready 이벤트: marathonTitle=$marathonTitle, totalMemberCount=$totalMemberCount")
                            if (marathonTitle != null) {
                                updateInitData(totalMemberCount, marathonTitle)
                            }
                        }
                        "/end" -> {
                            Log.d("MarathonDataViewModel", "End 이벤트 수신 → 타이머 정지 & 화면 이동")
                            stopTimer()
                            navController.navigate("endScreen")
                        }
                    }
                }
            }
        }
        dataClient.addListener(dataClientListener!!)
        Log.d("MarathonDataViewModel", "📡 Data Layer 이벤트 리스너 등록됨")
    }

    override fun onCleared() {
        super.onCleared()
        stopTimer()
        appContext?.let { ctx ->
            dataClientListener?.let { listener ->
                Wearable.getDataClient(ctx).removeListener(listener)
            }
        }
    }
}
