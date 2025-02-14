package com.example.gogoma.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gogoma.data.api.RetrofitInstance
import com.example.gogoma.data.dto.UserMarathonDetailDto
import com.example.gogoma.data.model.UserMarathonDetailResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegistDetailViewModel: ViewModel() {
    private val _userMarathonDetail = MutableStateFlow<UserMarathonDetailDto?>(null)
    val userMarathonDetail: StateFlow<UserMarathonDetailDto?> = _userMarathonDetail

    fun getUserMarathonById(accessToken: String,  id: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.userMarathonApiService.getUserMarathonById(accessToken, id)

                if (response.isSuccessful) {
                    response.body()?.let { userMarathonDetailResponse ->
                        _userMarathonDetail.value = userMarathonDetailResponse.userMarathonDetailDto
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