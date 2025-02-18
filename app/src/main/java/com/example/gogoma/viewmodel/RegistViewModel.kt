package com.example.gogoma.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gogoma.data.api.RetrofitInstance
import com.example.gogoma.data.dto.UserMarathonSearchDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RegistViewModel : ViewModel() {
    // 상태를 StateFlow로 변경
    private val _registList = MutableStateFlow<List<UserMarathonSearchDto>>(emptyList())
    val registList: StateFlow<List<UserMarathonSearchDto>> get() = _registList

    // 출발 전 (D+가 포함되지 않은 항목)
    val beforeStartList = registList.map { list ->
        list.filter { it.dday?.contains("D+") != true }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // 도착 후 (D+가 포함된 항목)
    val afterFinishList = registList.map { list ->
        list.filter { it.dday?.contains("D+") == true }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addRegist(newRegist: UserMarathonSearchDto) {
        _registList.value = _registList.value + newRegist
    }

    fun getUserMarathonList(accessToken: String) {
        viewModelScope.launch {
            try {
                val response =
                    RetrofitInstance.userMarathonApiService.getUserMarathonList(accessToken)

                if (response.isSuccessful) {
                    val userMarathonResponse = response.body()
                    if (userMarathonResponse?.userMarathonSearchDtoList != null) {
                        _registList.value = userMarathonResponse.userMarathonSearchDtoList
                        println( "✅ 사용자 마라톤 리스트 업데이트 성공")
                    } else {
                        _registList.value = emptyList()
                        println("⚠️ 응답 본문이 null이므로 빈 리스트로 설정")
                    }
                }
                else {
                    // HTTP 오류 응답 처리
                    println("API 호출 실패: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                // 네트워크 오류 등 예외 처리
                println("데이터를 가져오는 중 예외 발생: ${e.localizedMessage}")
            }
        }
    }

    fun notifyFriends(accessToken: String, marathonId: Int) {
        viewModelScope.launch {
            try {
                Log.d("tokentokne",accessToken)
                Log.d("marathonId",accessToken)
                val response = RetrofitInstance.userApiService.sendPushNotification(accessToken, marathonId)

                if (response.isSuccessful) {
                    Log.d("FCM", "푸시 알림 성공! 친구들에게 알림이 전송됨")
                } else {
                    Log.e("FCM", "푸시 알림 실패: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("FCM", "푸시 알림 요청 중 오류 발생: ${e.message}")
            }
        }
    }

}
