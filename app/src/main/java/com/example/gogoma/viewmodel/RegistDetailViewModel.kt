package com.example.gogoma.viewmodel

import androidx.lifecycle.ViewModel
import com.example.gogoma.data.api.RetrofitInstance

class RegistDetailViewModel: ViewModel() {

    private suspend fun getUserMarathonById(accessToken: String, marathonId: Int) {
        try {
            val response = RetrofitInstance.userMarathonApiService.getUserMarathonById(accessToken, marathonId)

            if (response.isSuccessful) {
                response.body()?.let { userMarathonDetailResponse ->
                    // userMarathonDetailResponse는 UserMarathonDetailResponse 타입이며,
                    // 내부에 userMarathonDetail: UserMarathonDetailDto 가 존재합니다.
                    val detail = userMarathonDetailResponse.userMarathonDetail

                    // 각 필드에 접근하여 원하는 처리를 할 수 있습니다.
                    println("Marathon: ${detail.marathon}")
                    println("Course Type List: ${detail.courseTypeList}")
                    println("Payment Type: ${detail.paymentType}")
                    println("Payment Amount: ${detail.paymentAmount}")
                    println("Payment DateTime: ${detail.paymentDateTime}")
                    println("Address: ${detail.address}")
                    println("Selected Course Type: ${detail.selectedCourseType}")

                    // 여기서 detail 데이터를 LiveData에 업데이트하거나 UI에 반영할 수 있습니다.
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