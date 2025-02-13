package com.example.gogoma.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gogoma.GlobalApplication
import com.example.gogoma.data.api.RetrofitInstance
import com.example.gogoma.data.model.MarathonStartInitDataResponse
import com.example.gogoma.data.model.UpcomingMarathonInfoResponse
import kotlinx.coroutines.launch

class PaceViewModel(private val globalApplication: GlobalApplication): ViewModel() {

    var marathonStartInitDataResponse by mutableStateOf<MarathonStartInitDataResponse?>(null)
        private set

    var upcomingMarathonInfoResponse by mutableStateOf<UpcomingMarathonInfoResponse?>(null)
        private set


    fun getInitData(accessToken: String, marathonId: Int) {
        viewModelScope.launch {
            try {
                // RetrofitInstance를 사용하여 API 호출
                val response = RetrofitInstance.watchApiService.getMarathonStartInitData(accessToken,marathonId)
                if (response.isSuccessful) {
                    marathonStartInitDataResponse = response.body()
                    globalApplication.initData = marathonStartInitDataResponse
                } else {
                    // 실패 시 처리 (예: 로그 찍기, 에러 메시지 출력 등)
                    marathonStartInitDataResponse = null
                }
            } catch (e: Exception) {
                // 예외 처리 (예: 네트워크 에러 등)
                marathonStartInitDataResponse = null
            }
        }
    }

    fun getUpcomingMarathonInfo(accessToken: String){
        viewModelScope.launch {
            try {
                val response =
                    RetrofitInstance.marathonApiService.getUpcomingMarathonInfo(accessToken)
                if (response.isSuccessful) {
                    upcomingMarathonInfoResponse = response.body()
                } else {
                    upcomingMarathonInfoResponse = null
                }
            } catch (e: Exception) {
                upcomingMarathonInfoResponse = null
            }
        }
    }

}