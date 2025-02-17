package com.example.gogoma.presentation.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
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
import kotlinx.coroutines.launch
import kotlin.random.Random

class MarathonDataViewModel : ViewModel() {

    // 기존 MarathonData 상태
    var _marathonState = mutableStateOf(
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
    val marathonState: State<MarathonData> = _marathonState

    // 현재 인덱스 상태 추가
    private val _currentIndex = mutableStateOf(0)
    val currentIndex: State<Int> = _currentIndex

    // 시간 추적 (초 단위)
    private val _elapsedTime = mutableStateOf(0)
    val elapsedTime: State<Int> = _elapsedTime

    // 색상 상태 추가 (Compose UI의 Color 사용)
    private val _currentColor = mutableStateOf(androidx.compose.ui.graphics.Color.Gray)
    val currentColor: State<androidx.compose.ui.graphics.Color> = _currentColor

    // Data Layer 이벤트 리스너 관련 변수
    private var dataClientListener: DataClient.OnDataChangedListener? = null
    private var appContext: Context? = null

    init {
        // 5초마다 currentPace 갱신
        viewModelScope.launch {
            while (true) {
                delay(5000L)
                updateCurrentPace()
                updateColor() // 색상 업데이트
            }
        }

        // 1초마다 distance 갱신
        viewModelScope.launch {
            while (_marathonState.value.currentDistance < _marathonState.value.totalDistance) {
                delay(1000L)
                updateCurrentDistance()
            }
        }

        // 1초마다 시간 갱신
        viewModelScope.launch {
            while (true) {
                delay(1000L)
                updateElapsedTime()
            }
        }
    }

    private fun generateRandomPace(): Int {
        return (270..450).random() // 4분 30초(270초) ~ 7분 30초(450초) 범위에서 랜덤 선택
    }

    // 인덱스 변경 함수
    fun nextStatus(total: Int) {
        _currentIndex.value = (_currentIndex.value + 1) % total
    }

    // 현재 페이스 업데이트
    private fun updateCurrentPace() {
        val newPace = generateRandomPace()
        _marathonState.value = _marathonState.value.copy(
            currentPace = newPace
        )
    }

    // 거리 업데이트
    private fun updateCurrentDistance() {
        val increment = Random.nextInt(500, 2001) // 500cm ~ 2000cm 범위 (5m ~ 20m)
        val newDistance = (_marathonState.value.currentDistance + increment)
            .coerceAtMost(_marathonState.value.totalDistance)
        val newRate = newDistance.toFloat() / _marathonState.value.totalDistance

        _marathonState.value = _marathonState.value.copy(
            currentDistance = newDistance,
            currentDistanceRate = newRate
        )
    }

    // 경과 시간 업데이트
    private fun updateElapsedTime() {
        val newTime = _marathonState.value.currentTime + 1
        _marathonState.value = _marathonState.value.copy(currentTime = newTime)
        _elapsedTime.value = newTime
    }

    // 색상 업데이트 (현재 페이스와 목표 페이스 비교)
    private fun updateColor() {
        val currentPace = _marathonState.value.currentPace
        val targetPace = _marathonState.value.targetPace

        val paceDifference = currentPace - targetPace

        val color = when {
            _marathonState.value.state.equals("G") -> androidx.compose.ui.graphics.Color.Green
            _marathonState.value.state.equals("Y") -> androidx.compose.ui.graphics.Color.Yellow
            else -> androidx.compose.ui.graphics.Color.Red
        }

        _currentColor.value = color
    }

    // 초기 데이터 업데이트 (예: /ready 이벤트 처리)
    fun updateInitData(totalMemberCount: Int, marathonTitle: String) {
        _marathonState.value = _marathonState.value.copy(
            totalMemberCount = totalMemberCount,
            marathonTitle = marathonTitle
        )
    }

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

                            Log.d("marathon", "update 이벤트가 발생했습니다.")

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
