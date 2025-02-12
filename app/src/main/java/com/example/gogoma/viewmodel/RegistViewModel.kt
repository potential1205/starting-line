package com.example.gogoma.viewmodel

import androidx.lifecycle.ViewModel
import com.example.gogoma.data.api.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.gogoma.ui.components.Regist
import retrofit2.Retrofit

class RegistViewModel : ViewModel() {
    private val _registList = MutableStateFlow<List<Regist>>(emptyList())
    val registList: StateFlow<List<Regist>> = _registList

    fun addRegist(newRegist: Regist) {
        _registList.value = _registList.value + newRegist
    }

    private suspend fun getUserMarathonList(accessToken: String) {
        try {
            val response = RetrofitInstance.userMarathonApiService.getUserMarathonList(accessToken)

            if (response.isSuccessful) {
                response.body()?.let { userMarathonResponse ->
                    userMarathonResponse.userMarathons.forEach { marathon ->
                        // 각 항목(UserMarathonSearchDto)의 필드 접근 예시
                        println("ID: ${marathon.userMarathonId}")
                        println("Title: ${marathon.marathonTitle}")
                        println("Type: ${marathon.marathonType}")
                        println("D-Day: ${marathon.dDay}")
                        println("Race Start: ${marathon.raceStartDateTime}")
                        println("Payment Date: ${marathon.paymentDateTime}")
                    }
                } ?: run {
                    // 응답 본문이 null일 경우 처리
                    println("응답 본문이 null입니다.")
                }
            } else {
                // HTTP 오류 응답 처리
                println("API 호출 실패: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            // 네트워크 오류 등 예외 처리
            println("데이터를 가져오는 중 예외 발생: ${e.localizedMessage}")
        }
    }





}
