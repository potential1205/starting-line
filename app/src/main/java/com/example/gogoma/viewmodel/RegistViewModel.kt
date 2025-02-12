package com.example.gogoma.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gogoma.data.api.RetrofitInstance
import com.example.gogoma.data.dto.UserMarathonDetailDto
import com.example.gogoma.data.dto.UserMarathonSearchDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit

class RegistViewModel : ViewModel() {
    // 상태를 StateFlow로 변경
    private val _registList = MutableStateFlow<List<UserMarathonSearchDto>>(emptyList())
    val registList: StateFlow<List<UserMarathonSearchDto>> get() = _registList

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
                    if (userMarathonResponse?.userMarathons != null) {
                        _registList.value = userMarathonResponse.userMarathons
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
}
