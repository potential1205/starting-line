package com.example.gogoma.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gogoma.data.api.RetrofitInstance
import com.example.gogoma.data.model.Marathon
import com.example.gogoma.data.model.MarathonDetailResponse
import com.example.gogoma.data.model.MarathonSearchResponse
import com.example.gogoma.data.model.MarathonType
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime

class MarathonDetailViewModel : ViewModel() {
    var marathonDetail by mutableStateOf<MarathonDetailResponse?>(null)
        private set

    fun loadMarathonDetail(marathonId: Int) {
        viewModelScope.launch {
            try {
                // RetrofitInstance를 사용하여 API 호출
                val response = RetrofitInstance.marathonApiService.getMarathonById(marathonId)
                if (response.isSuccessful) {
                    marathonDetail = response.body()
                } else {
                    // 실패 시 처리 (예: 로그 찍기, 에러 메시지 출력 등)
                    marathonDetail = null
                }
            } catch (e: Exception) {
                // 예외 처리 (예: 네트워크 에러 등)
                marathonDetail = null
            }
        }
    }
}