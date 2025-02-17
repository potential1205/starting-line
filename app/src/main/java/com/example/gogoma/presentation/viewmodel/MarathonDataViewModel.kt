package com.example.gogoma.presentation.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gogoma.presentation.data.FriendInfo
import com.example.gogoma.presentation.data.MarathonData
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.Wearable
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class MarathonDataViewModel : ViewModel() {

    // 기존 MarathonData 상태
    var _marathonState = MutableStateFlow(
        MarathonData(
            time = System.currentTimeMillis(),
            totalDistance = 20000, // 예제: 2km (cm 단위)
            currentDistance = 0,
            currentDistanceRate = 0f,
            targetPace = 330, // 목표 페이스 (초)
            currentPace = 330, // 현재 페이스 (초)
            targetTime = 330 * 2,
            currentTime = 0,
            state = "running",
            myRank = 1,
            totalMemberCount = 5,
            friendInfoList = emptyList(),
            marathonTitle = ""
        )
    )
    val marathonState: StateFlow<MarathonData> = _marathonState

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

    init {
        // 1초마다 시간 갱신
        viewModelScope.launch {
            while (true) {
                delay(1000L)
                updateElapsedTime()
            }
        }
    }

//    // 예: 상태 업데이트
//    fun updateMarathonState(updatedState: MarathonData) {
//        _marathonState.value = updatedState // 새로운 상태로 업데이트
//    }

    // 인덱스 변경 함수
    fun nextStatus(total: Int) {
        _currentIndex.value = (_currentIndex.value + 1) % total
    }

    // 경과 시간 업데이트
    private fun updateElapsedTime() {
        val newTime = _marathonState.value.currentTime + 1
        _marathonState.value = _marathonState.value.copy(currentTime = newTime)
        _elapsedTime.value = newTime
    }

    // 상태에 따라 currentColor 변경
    fun updateMarathonState(updatedState: MarathonData) {
        _marathonState.value = updatedState  // MarathonData 상태 변경

        // ✅ 상태에 따라 currentColor 변경
        _currentColor.value = when (updatedState.state) {
            "G" -> Color.Green
            "Y" -> Color.Yellow
            else -> Color.Red
        }
    }

    // 초기 데이터 업데이트 (예: /ready 이벤트 처리)
    fun updateInitData(totalMemberCount: Int, marathonTitle: String) {
        _marathonState.value = _marathonState.value.copy(
            totalMemberCount = totalMemberCount,
            marathonTitle = marathonTitle
        )
    }

    var idx = 1
    fun startDataListener(context: Context) {
        appContext = context.applicationContext
        val dataClient = Wearable.getDataClient(appContext!!)
        val gson = Gson()
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

                            Log.d("marathon", "update 이벤트가 발생했습니다."+targetTime+", idx: "+idx++)

                            _marathonState.value = state?.let {
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
                            }!!



//                            state?.let {
//                                updateMarathonState(
//                                    _marathonState.value.copy(
//                                        totalDistance = totalDistance,
//                                        targetPace = targetPace,
//                                        targetTime = targetTime,
//                                        currentDistance = currentDistance,
//                                        currentDistanceRate = currentDistanceRate,
//                                        currentPace = currentPace,
//                                        currentTime = currentTime,
//                                        myRank = myRank,
//                                        totalMemberCount = totalMemberCount,
//                                        friendInfoList = friendInfoList,
//                                        state = it
//                                    )
//                                )
//                            }

                            Log.d("marathon", _marathonState.value.totalDistance.toString())
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
                    }
                }
            }
        }
        dataClient.addListener(dataClientListener!!)
        Log.d("MarathonDataViewModel", "📡 Data Layer 이벤트 리스너 등록됨")
    }

    override fun onCleared() {
        super.onCleared()
        appContext?.let { ctx ->
            dataClientListener?.let { listener ->
                Wearable.getDataClient(ctx).removeListener(listener)
            }
        }
    }
}
