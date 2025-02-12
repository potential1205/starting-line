package com.example.gogoma.viewmodel

import android.content.Context
import android.media.session.MediaSession.Token
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gogoma.data.api.RetrofitInstance
import com.example.gogoma.data.dto.KakaoPayApproveRequest
import com.example.gogoma.data.dto.KakaoPayApproveResponse
import com.example.gogoma.data.dto.KakaoPayReadyRequest
import com.example.gogoma.data.dto.KakaoPayReadyResponse
import com.example.gogoma.data.model.Address
import com.example.gogoma.ui.components.Regist
import com.example.gogoma.utils.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class PaymentViewModel : ViewModel() {
    // 저장된 주소 리스트
    private val _addressList = MutableStateFlow(loadSavedAddresses())
    val addressList: StateFlow<List<Address>> = _addressList

    // 현재 선택된 주소 (기본값: 기본 배송지)
    private val _selectedAddress = MutableStateFlow(_addressList.value.firstOrNull { it.isDefault })
    val selectedAddress: StateFlow<Address?> = _selectedAddress

    // 현재 선택된 사이즈
    private val _selectedSize = MutableStateFlow("95")
    val selectedSize: StateFlow<String> = _selectedSize

    // 참가 종목 선택 상태
    private val _selectedDistance = MutableStateFlow("5km")
    val selectedDistance: StateFlow<String> = _selectedDistance

    // 결제 수단 선택 상태
    private val _selectedPayment = MutableStateFlow("카카오페이")
    val selectedPayment: StateFlow<String> = _selectedPayment

    // 약관 동의 상태
    private val _isAgreementChecked = MutableStateFlow(false)
    val isAgreementChecked: StateFlow<Boolean> = _isAgreementChecked

    private val _registInfo = MutableStateFlow<Regist?>(null)
    val registInfo: StateFlow<Regist?> = _registInfo.asStateFlow()

    private val paymentApi = RetrofitInstance.paymentApiService

    private val _kakaoPayReadyRequest = MutableStateFlow<KakaoPayReadyRequest?>(null)
    val kakaoPayReadyRequest: StateFlow<KakaoPayReadyRequest?> = _kakaoPayReadyRequest.asStateFlow()

    private val _kakaoPayReadyResponse = MutableStateFlow<KakaoPayReadyResponse?>(null)
    val kakaoPayReadyResponse: StateFlow<KakaoPayReadyResponse?> = _kakaoPayReadyResponse

    private val _kakaoPayApproveResponse = MutableStateFlow<KakaoPayApproveResponse?>(null)
    val kakaoPayApproveResponse: StateFlow<KakaoPayApproveResponse?> = _kakaoPayApproveResponse

    private val _paymentResult = MutableStateFlow<String?>(null)
    val paymentResult: StateFlow<String?> = _paymentResult

    private val _isPaymentSuccessful = MutableStateFlow(false)
    val isPaymentSuccessful: StateFlow<Boolean> = _isPaymentSuccessful

    // 배송지 선택 업데이트
    fun selectAddress(address: Address) {
        viewModelScope.launch {
            _selectedAddress.value = address
        }
    }

    // 사이즈 선택 업데이트
    fun updateSelectedSize(newSize: String) {
        viewModelScope.launch {
            _selectedSize.value = newSize
        }
    }

    // 참가 종목 선택 업데이트
    fun updateSelectedDistance(newDistance: String) {
        viewModelScope.launch {
            _selectedDistance.value = newDistance
        }
    }

    // 결제 수단 선택 업데이트
    fun updateSelectedPayment(newPayment: String) {
        viewModelScope.launch {
            _selectedPayment.value = newPayment
        }
    }

    // 약관 동의 상태 업데이트
    fun updateAgreementChecked(isChecked: Boolean) {
        viewModelScope.launch {
            _isAgreementChecked.value = isChecked
        }
    }

    fun saveRegistInfo(regist: Regist) {
        _registInfo.value = regist
    }

    fun requestKakaoPayReady(request: KakaoPayReadyRequest, context: Context) {
        viewModelScope.launch {
            try {
                val token = TokenManager.getAccessToken(context)
                if(token.isNullOrEmpty()) {
                    Log.e("PaymentViewModel", "❌ Access Token이 존재하지 않습니다.")
                    return@launch
                }
                _kakaoPayReadyRequest.value = request  // 요청 데이터 저장
                Log.d("PaymentViewModel", "📌 카카오페이 결제 준비 요청 시작: $request")

                val response = paymentApi.requestKakaoPayReady(token, request)

                if (response.isSuccessful) {
                    _kakaoPayReadyResponse.value = response.body()
                    _kakaoPayReadyRequest.value = request  // 요청 정보 저장
                    Log.d("PaymentViewModel", "✅ 결제 준비 성공: ${response.body()}")
                } else {
                    Log.e("PaymentViewModel", "❌ 결제 준비 실패: HTTP ${response.code()} - ${response.errorBody()?.string()}")
                }
            } catch (e: HttpException) {
                Log.e("PaymentViewModel", "❌ HTTP 오류 발생: ${e.message}", e)
            } catch (e: IOException) {
                Log.e("PaymentViewModel", "❌ 네트워크 오류 발생: ${e.message}", e)
            } catch (e: Exception) {
                Log.e("PaymentViewModel", "❌ 알 수 없는 오류 발생: ${e.message}", e)
            }
        }
    }

    fun requestKakaoPayApprove(pgToken: String, context: Context) {
        viewModelScope.launch {
            val token = TokenManager.getAccessToken(context)
            if (token.isNullOrEmpty()) {
                Log.e("PaymentViewModel", "❌ Access Token이 존재하지 않습니다.")
                return@launch
            }
            val tid = _kakaoPayReadyResponse.value?.tid ?: return@launch
            val readyRequest = _kakaoPayReadyRequest.value ?: return@launch  // 저장된 요청 정보 가져오기

            val request = KakaoPayApproveRequest(
                orderId = readyRequest.orderId,  // 저장된 orderId 사용
                tid = tid,
                pgToken = pgToken
            )

            try {
                Log.d("PaymentViewModel", "📌 카카오페이 결제 승인 요청 시작: $request")
                val response = paymentApi.requestKakaoPayApprove(token, request)

                if (response.isSuccessful) {
                    _kakaoPayApproveResponse.value = response.body()
                    Log.d("PaymentViewModel", "✅ 결제 승인 성공: ${response.body()}")
                    resetPaymentState() // 결제 후 초기화
                } else {
                    Log.e("PaymentViewModel", "❌ 결제 승인 실패: HTTP ${response.code()} - ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("PaymentViewModel", "❌ 결제 승인 오류 발생: ${e.message}", e)
            }
        }
    }

    fun handlePaymentRedirect(url: String, context: Context) {
        val pgToken = Uri.parse(url).getQueryParameter("pg_token")
        if (!pgToken.isNullOrEmpty()) {
            requestKakaoPayApprove(pgToken, context)
        } else {
            Log.e("PaymentViewModel", "❌ pg_token이 URL에 포함되어 있지 않습니다.")
        }
    }
    fun redirectAfterPayment(pgToken: String, redirect: String, context: Context, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                Log.d("PaymentViewModel", "📌 리다이렉트 요청 시작: pgToken=$pgToken, redirect=$redirect")
                val response = paymentApi.redirectAfterPayment(pgToken = pgToken, redirect = redirect)

                if (response.isSuccessful) {
                    Log.d("PaymentViewModel", "✅ 리다이렉트 성공")
                    onResult(true)
                    resetPaymentState() // 리다이렉트 성공 후 초기화
                } else if (response.code() == 302) {
                    // 302 리다이렉트 응답 처리
                    val redirectUrl = response.headers()["Location"]
                    if (redirectUrl != null) {
                        Log.d("PaymentViewModel", "📍 리다이렉트 URL 감지: $redirectUrl")
                        // 직접 결제 승인 처리
                        val newPgToken = Uri.parse(redirectUrl).getQueryParameter("pg_token")
                        if (!newPgToken.isNullOrEmpty()) {
                            requestKakaoPayApprove(newPgToken, context)
                            onResult(true)
                        } else {
                            onResult(false)
                        }
                    } else {
                        Log.e("PaymentViewModel", "❌ 리다이렉트 URL 없음")
                        onResult(false)
                    }
                } else {
                    Log.e("PaymentViewModel", "❌ 리다이렉트 실패: HTTP ${response.code()} - ${response.errorBody()?.string()}")
                    onResult(false)
                }
            } catch (e: Exception) {
                Log.e("PaymentViewModel", "❌ 리다이렉트 요청 오류 발생: ${e.message}", e)
                onResult(false)
            }
        }
    }

    fun resetPaymentState() {
        Log.d("PaymentViewModel", "결제 상태 초기화")
        _paymentResult.value = null
        _isPaymentSuccessful.value = false
        _kakaoPayReadyRequest.value = null
        _kakaoPayReadyResponse.value = null
        _kakaoPayApproveResponse.value = null
        _selectedDistance.value = ""
        _selectedPayment.value = ""
    }
    // 기본 주소 및 데이터 불러오기 함수
    companion object {
        fun loadSavedAddresses(): List<Address> {
            return listOf(
                Address("1", "홍길동", "서울특별시 영등포구 선유로 00 현대아파트", "101동 202호", "010-0000-0000", isDefault = true),
                Address("2", "김이름", "서울특별시 강남구 테헤란로 00", "302동 502호", "010-1234-5678"),
                Address("3", "박철수", "부산광역시 해운대구 달맞이길 00", "100동 1001호", "010-9876-5432")
            )
        }
    }
}
