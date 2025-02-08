package com.example.gogoma.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.gogoma.data.api.RetrofitInstance
import com.example.gogoma.data.model.CreateUserRequest
import com.example.gogoma.data.model.KakaoUserInfo
import com.example.gogoma.data.model.StatusResponse
import com.example.gogoma.utils.TokenManager
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class UserViewModel() : ViewModel() {
    var isLoading by mutableStateOf(false)
        private set

    var isLoggedIn by mutableStateOf(false)
        private set

    // 회원가입 로직을 위한 accessToken 임시 저장 변수
    var accessToken by mutableStateOf<String?>(null)
        private set
    var refreshToken by mutableStateOf<String?>(null)
        private set

    //서버로부터 반환받는 로그인/회원가입 상태(signup/login 문자열)
    var loginStatus by mutableStateOf("")
        private set

    // 미리 불러온 Kakao 사용자 정보 (회원가입 화면에서 기본값으로 사용)
    var kakaoUserInfo by mutableStateOf<KakaoUserInfo?>(null)
        private set

    fun init(context: Context) {
        //앱이 시작되면 SharedPreferences에서 저장된 access token과 refresh token을 확인
        accessToken = TokenManager.getAccessToken(context)
        refreshToken = TokenManager.getRefreshToken(context)

        if(accessToken != null && !TokenManager.isTokenExpired(context)) {
            isLoggedIn = true
        } else {
            refreshAccessToken(context)
        }
    }

    fun refreshAccessToken(context: Context) {
        val storedRefreshToken = TokenManager.getRefreshToken(context)
        if (storedRefreshToken != null) {
            // refresh token을 서버로 보내서 새로운 access token을 요청
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitInstance.userApiService.refreshAccessToken(storedRefreshToken)

                    if (response.isSuccessful) {
                        // 요청 성공 시 새로운 access token을 받아 TokenManager에 저장
                        val newAccessToken = response.body()?.access_token
                        val newRefreshToken = response.body()?.refresh_token

                        if (newAccessToken != null && newRefreshToken != null) {
                            val expirationTime = System.currentTimeMillis() + 3600000  // 만료 시간 1시간 후
                            TokenManager.saveTokens(context, newAccessToken, newRefreshToken, expirationTime)
                            // TokenManager에서 새로운 access token을 가져와 isLoggedIn을 true로 설정
                            accessToken = newAccessToken
                            isLoggedIn = true
                            Log.i("RefreshToken", "새로운 access token을 성공적으로 받았습니다.")
                        }
                    } else {
                        // 요청 실패 시 처리
                        Log.e("RefreshToken", "새로운 access token 요청 실패: ${response.code()}")
                        CoroutineScope(Dispatchers.Main).launch {
                            Toast.makeText(context, "새로운 access token 요청 실패", Toast.LENGTH_SHORT).show()
                        }
                        isLoggedIn = false
                    }
                } catch (e: Exception) {
                    Log.e("RefreshToken", "refreshAccessToken API 호출 실패", e)
                    CoroutineScope(Dispatchers.Main).launch {
                        Toast.makeText(context, "네트워크 오류 발생", Toast.LENGTH_SHORT).show()
                    }
                    isLoggedIn = false
                }
            }
        } else {
            // refresh token이 없으면 로그인이 필요
            isLoggedIn = false
        }
    }

    // 카카오 로그인 함수
    fun loginWithKakao(context: Context, callback: (Boolean) -> Unit) {
        isLoading = true

        // 공통 로그인 결과 처리
        val loginResultHandler: (token: com.kakao.sdk.auth.model.OAuthToken?, error: Throwable?) -> Unit =
            { token, error -> handleKakaoResponse(token, error, context, callback) }

        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context, callback = loginResultHandler)
        } else {
            UserApiClient.instance.loginWithKakaoAccount(context, callback = loginResultHandler)
        }
    }


    //카카오 로그인 응답 처리
    private fun handleKakaoResponse(
        token: com.kakao.sdk.auth.model.OAuthToken?,
        error: Throwable?,
        context: Context,
        callback: (Boolean) -> Unit
    ) {
        when {
            error != null -> {
                Log.e("KakaoLogin", "응답 실패", error)
                Toast.makeText(context, "응답 실패: ${error.message}", Toast.LENGTH_SHORT).show()
                isLoggedIn = false
                callback(false)
            }
            token != null -> {
                Log.i("KakaoLogin", "응답 성공: ${token.accessToken}")
                isLoggedIn = true
                accessToken = token.accessToken  // 토큰 저장

                val expirationTime = System.currentTimeMillis() + 3600000  // 만료 시간 1시간 후
                TokenManager.saveTokens(context, token.accessToken, token.refreshToken, expirationTime)

                // accessToken과 함께 서버에 회원가입/로그인 판단 요청
                // 로그인 후 카카오 사용자 정보 가져오기
                getKakaoUserInfo(context, token.accessToken) { userInfo ->
                    if (userInfo != null) {
                        kakaoUserInfo = userInfo
                        Log.i("KakaoUserInfo", "사용자 정보: ${userInfo.name}")
                    } else {
                        Log.e("KakaoUserInfo", "사용자 정보 불러오기 실패")
                    }
                }
                checkLoginOrSignUp(context, token.accessToken)
                callback(true)
            }
        }
        isLoading = false
    }

    //로그인/회원가입 판단 API 호출
    private fun checkLoginOrSignUp(context: Context, accessToken: String) {
        isLoading = true
        // 서버로부터 로그인/회원가입 상태를 확인 받음
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.userApiService.determineLoginOrSignUp(accessToken)
                handleLoginOrSignUpResponse(response)
            } catch (e: Exception) {
                Log.e("LoginOrSignUp", "API 호출 실패", e)
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(context, "회원가입/로그인 판단 실패", Toast.LENGTH_SHORT).show()
                }
                isLoading = false
            }
        }
    }

    //로그인/회원가입 판단 API 응답 처리
    private fun handleLoginOrSignUpResponse(response: Response<StatusResponse>) {
        if (response.isSuccessful) {
            val statusResponse = response.body()
            if (statusResponse != null) {
                loginStatus = statusResponse.status
            }
        } else {
            Log.e("LoginOrSignUp", "응답 실패: ${response.code()}")
        }
        isLoading = false
    }

    // 카카오에서 사용자 정보를 가져오는 함수
    fun getKakaoUserInfo(context: Context, accessToken: String, callback: (KakaoUserInfo?) -> Unit) {
        isLoading = true
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.userApiService.getKakaoUserInfo(accessToken)
                if (response.isSuccessful) {
                    kakaoUserInfo = response.body()
                    CoroutineScope(Dispatchers.Main).launch {
                        callback(kakaoUserInfo)
                    }
                } else {
                    Log.e("KakaoUserInfo", "응답 실패: ${response.code()}")
                    CoroutineScope(Dispatchers.Main).launch {
                        callback(null)
                    }
                }
            } catch (e: Exception) {
                Log.e("KakaoUserInfo", "API 호출 실패", e)
                CoroutineScope(Dispatchers.Main).launch {
                    callback(null)
                }
            }
            isLoading = false
        }
    }

    // 서비스 회원가입
    fun signUpUser(context: Context, createUserRequest: CreateUserRequest, callback: (Boolean) -> Unit) {
        isLoading = true
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.userApiService.signup(createUserRequest)
                if (response.isSuccessful) {
                    // 회원가입 성공 시 UI 스레드로 결과 전달
                    CoroutineScope(Dispatchers.Main).launch {
                        Toast.makeText(context, "회원가입 성공", Toast.LENGTH_SHORT).show()
                        callback(true)
                    }
                } else {
                    Log.e("SignUp", "응답 실패: ${response.code()}")
                    CoroutineScope(Dispatchers.Main).launch {
                        Toast.makeText(context, "회원가입 실패", Toast.LENGTH_SHORT).show()
                        callback(false)
                    }
                }
            } catch (e: Exception) {
                Log.e("SignUp", "API 호출 실패", e)
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(context, "회원가입 중 오류 발생", Toast.LENGTH_SHORT).show()
                    callback(false)
                }
            }
            isLoading = false
        }
    }



}