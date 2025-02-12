package com.example.gogoma.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gogoma.data.api.RetrofitInstance
import com.example.gogoma.data.dto.UserMarathonDetailDto
import com.example.gogoma.data.model.UserMarathonDetailResponse
import kotlinx.coroutines.launch

class RegistDetailViewModel: ViewModel() {
    var userMarathonDetail by mutableStateOf<UserMarathonDetailDto?>(null)
        private set

    fun getUserMarathonById(accessToken: String, marathonId: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.userMarathonApiService.getUserMarathonById(accessToken, marathonId)

                if (response.isSuccessful) {
                    response.body()?.let { userMarathonDetailResponse ->
                        userMarathonDetail = userMarathonDetailResponse.userMarathonDetail
                    } ?: run {
                        println("응답 본문이 null입니다.")
                    }
                } else {
                    println("API 호출 실패: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                println("데이터를 가져오는 중 예외 발생: ${e.localizedMessage}")
            }
        }
    }


}