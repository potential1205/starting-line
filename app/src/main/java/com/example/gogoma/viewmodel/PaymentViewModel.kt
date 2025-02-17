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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gogoma.utils.TokenManager
import com.example.gogoma.data.api.RetrofitInstance
import com.example.gogoma.data.dto.KakaoPayApproveRequest
import com.example.gogoma.data.dto.KakaoPayApproveResponse
import com.example.gogoma.data.dto.KakaoPayReadyRequest
import com.example.gogoma.data.dto.KakaoPayReadyResponse
import com.example.gogoma.data.dto.UserMarathonSearchDto
import com.example.gogoma.data.model.Address
import com.example.gogoma.data.model.CreateUserMarathonRequest
import com.example.gogoma.data.model.PaymentType
import com.google.gson.Gson
import com.kakao.sdk.user.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Callback
import retrofit2.HttpException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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

    private val _selectedPrice = MutableStateFlow(0)
    val selectedPrice: StateFlow<Int> = _selectedPrice

    // 약관 동의 상태
    private val _isAgreementChecked = MutableStateFlow(false)
    val isAgreementChecked: StateFlow<Boolean> = _isAgreementChecked

    private val _registInfo = MutableStateFlow<UserMarathonSearchDto?>(null)
    val registInfo: StateFlow<UserMarathonSearchDto?> = _registInfo.asStateFlow()

    private val paymentApi = RetrofitInstance.paymentApiService

    private val userMarathonApi = RetrofitInstance.userMarathonApiService

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

    fun updateSelectedPrice(price: Int) {
        viewModelScope.launch {
            _selectedPrice.value = price
        }
    }

    // 약관 동의 상태 업데이트
    fun updateAgreementChecked(isChecked: Boolean) {
        viewModelScope.launch {
            _isAgreementChecked.value = isChecked
        }
    }

    fun saveRegistInfo(regist: UserMarathonSearchDto, title: String) {
        viewModelScope.launch {
            val updatedRegist = regist.copy(marathonTitle = title)
            _registInfo.value = updatedRegist
            Log.d("PaymentViewModel", "📌 [제목 저장 완료]: $title")
        }
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

            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.e("PaymentViewModel", "❌ [API 오류] HTTP 400 발생! 서버 응답: $errorBody", e)
                onResult(false)
            } catch (e: Exception) {
                Log.e("PaymentViewModel", "❌ 리다이렉트 요청 오류 발생: ${e.message}", e)
                onResult(false)
            }
        }
    }

    fun getRegistFromJson(json: String): CreateUserMarathonRequest? {
        return try {
            val dto = Gson().fromJson(json, UserMarathonSearchDto::class.java)

            Log.d("PaymentViewModel", "📥 [JSON 파싱 결과]: $dto")

            val marathonId = dto.userMarathonId ?: run {
                Log.e("PaymentViewModel", "🚨 Marathon ID가 null 또는 0입니다.")
                return null
            }

            val paymentAmount = _selectedPrice.value.toString().takeIf { it.isNotBlank() } ?: run {
                Log.e("PaymentViewModel", "🚨 PaymentAmount가 비어 있습니다.")
                return null
            }

            val courseType = dto.marathonType ?: run {
                Log.e("PaymentViewModel", "🚨 CourseType이 null입니다.")
                return null
            }
            val paymentDate = try {
                val now = Date()
                val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.KOREA)
                sdf.format(now)
            } catch (e: Exception) {
                Log.e("PaymentViewModel", "❌ 날짜 변환 실패: ${e.message}", e)
                return null
            }

            val raceDate = try {
                val sdf = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA)
                val raceDate = sdf.parse(dto.raceStartDateTime!!)
                val today = Date()
                val diff = (raceDate.time - today.time) / (1000 * 60 * 60 * 24)
                diff.toInt()
            } catch (e: Exception) {
                Log.e("PaymentViewModel", "❌ D-Day 계산 실패: ${e.message}", e)
                return null
            }

            if (marathonId == null || marathonId <= 0) {
                Log.e("PaymentViewModel", "🚨 [오류] 유효하지 않은 marathonId: $marathonId")
                return null
            }

            CreateUserMarathonRequest(
                marathonId = marathonId,
                address = selectedAddress.value?.let { "${it.address} ${it.detailAddress}" } ?: "주소 미입력",
                paymentType = PaymentType.KAKAO_PAY,
                paymentAmount = paymentAmount,
                paymentDateTime = paymentDate,
                courseType = courseType
            ).also {
                Log.d("PaymentViewModel", "✅ CreateUserMarathonRequest 생성: $it, D-Day: $raceDate")
            }
        } catch (e: Exception) {
            Log.e("PaymentViewModel", "❌ JSON 파싱 실패: ${e.message}", e)
            null
        }
    }

    fun checkAndRegisterMarathon(
        regist: CreateUserMarathonRequest,
        context: Context,
        callback: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val accessToken = TokenManager.getAccessToken(context) ?: run {
                    Log.e("PaymentViewModel", "❌ 토큰이 null입니다.")
                    callback(false)
                    return@launch
                }

                val json = Gson().toJson(regist)
                Log.d("PaymentViewModel", "📤 [API 요청 전송]: $json")

                Log.d("PaymentViewModel", "[API 호출] 중복 체크 시작 (마라톤 ID: ${regist.marathonId})")

                val duplicateResponse =
                    userMarathonApi.checkDuplicateUserMarathon(accessToken, regist.marathonId)
                Log.d("PaymentViewModel", "🛠️ [중복 체크 응답] 성공 여부: ${duplicateResponse.success}")

                if (duplicateResponse.success) {
                    Log.d("PaymentViewModel", "✅ [중복 체크] 등록 진행 가능")

                    val response = userMarathonApi.registerUserMarathon(accessToken, regist)

                    if (response.success) {
                        Log.d("PaymentViewModel", "🎯 [등록 성공] 마라톤 등록 완료")
                        callback(true)
                    } else {
                        callback(false)
                    }
                } else {
                    Log.d("PaymentViewModel", "⚠️ [중복 탐지] 이미 등록된 마라톤.")
                    callback(false)
                }
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.e("PaymentViewModel", "❌ [API 오류] HTTP 400 발생! 서버 응답: $errorBody", e)
                callback(false)
            } catch (e: IOException) {
                Log.e("PaymentViewModel", "❌ [네트워크 오류] ${e.message}", e)
            } catch (e: Exception) {
                Log.e("PaymentViewModel", "❌ [API 오류] 마라톤 등록 실패: ${e.message}", e)
                e.printStackTrace()
                callback(false)
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
