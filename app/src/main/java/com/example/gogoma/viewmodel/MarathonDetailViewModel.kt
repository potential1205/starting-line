package com.example.gogoma.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gogoma.data.api.RetrofitInstance
import com.example.gogoma.data.model.MarathonDetailResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MarathonDetailViewModel : ViewModel() {
    private val _marathonDetail = MutableStateFlow<MarathonDetailResponse?>(null)
    val marathonDetail: StateFlow<MarathonDetailResponse?> = _marathonDetail

    fun loadMarathonDetail(marathonId: Int) {
        Log.d("marathonId",marathonId.toString())
        viewModelScope.launch {
            try {
                // RetrofitInstance를 사용하여 API 호출
                val response = RetrofitInstance.marathonApiService.getMarathonById(marathonId)
                if (response.isSuccessful) {
                    _marathonDetail.value = response.body()
                } else {
                    // 실패 시 처리 (예: 로그 찍기, 에러 메시지 출력 등)
                    _marathonDetail.value = null
                }
            } catch (e: Exception) {
                // 예외 처리 (예: 네트워크 에러 등)
                _marathonDetail.value = null
            }
        }
    }
}