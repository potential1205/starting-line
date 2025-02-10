package com.example.gogoma.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    // 회원가입 로직을 위한 임시 저장 변수
    var tmpAccessToken by mutableStateOf<String?>(null)
        private set
    var tmpRefreshToken by mutableStateOf<String?>(null)
        private set
    var tmpkakaoUserInfo by mutableStateOf<KakaoUserInfo?>(null)
        private set

    //서버로부터 반환받는 로그인/회원가입 상태(signup/login 문자열)
    var loginStatus by mutableStateOf("")
        private set

    // 로그인하면서 저장하는 실제 사용자 정보
    var kakaoUserInfo by mutableStateOf<KakaoUserInfo?>(null)
        private set

    fun init(context: Context) {
        //앱이 시작되면 Token 유효성을 확인
        CoroutineScope(Dispatchers.Main).launch {
            val isValid = checkTokenValidity(context)
            if(isValid){//토큰이 유효함
                isLoggedIn = true //로그인 처리
            } else {//유효하지 않음
                isLoggedIn = false
                refreshAccessToken(context) //토큰 갱신하기
            }
        }
    }

    suspend fun checkTokenValidity(context: Context):Boolean {
        val accessToken = TokenManager.getAccessToken(context)
        
        //토큰이 비어 있지 않고, 만료되지 않았다면
        if(accessToken != null && TokenManager.isTokenExpired(context)){
            //로그인을 했는지 확인
            try {
                val response = RetrofitInstance.userApiService.determineLoginOrSignUp(accessToken)
                
                if(response.isSuccessful) {
                    val status = response.body()?.status
                    return status == "login" //서비스에 가입했다면 login을 반환함, 이 경우 true
                } else {
                    //호출 실패
                    return false
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return false
            }
        } else {
            //토큰이 없거나 만료됨
            return false
        }
    }

    //임시 회원 탈퇴
    fun unlinkKakaoAccount(callback: (Boolean, Throwable?) -> Unit) {
        UserApiClient.instance.unlink { error ->
            if (error != null) {
                callback(false, error)
            } else {
                callback(true, null)
            }
        }
    }


    // 토큰 갱신 처리
    fun refreshAccessToken(context: Context) {
        val storedRefreshToken = TokenManager.getRefreshToken(context) ?:return logout(context)
        isLoading = true

        viewModelScope.launch {
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
                        isLoggedIn = true
                        Log.i("RefreshToken", "새로운 access token을 성공적으로 받았습니다.")
                    }
                } else {
                    // 요청 실패 시 처리
                    Log.e("RefreshToken", "새로운 access token 요청 실패: ${response.code()}")
                    Toast.makeText(context, "새로운 access token 요청 실패", Toast.LENGTH_SHORT).show()
                    logout(context)
                }
            } catch (e: Exception) {
                Log.e("RefreshToken", "refreshAccessToken API 호출 실패", e)
                Toast.makeText(context, "네트워크 오류 발생", Toast.LENGTH_SHORT).show()
                logout(context)
            } finally {
                isLoading = false
            }
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
                println("처음 토큰을 받았을 때 액세스"+token.accessToken)
                Log.i("KakaoLogin", "응답 성공: ${token.accessToken}")

                // accessToken과 함께 서버에 회원가입/로그인 판단 요청
                checkLoginOrSignUp(context, token.accessToken, token.refreshToken)
                callback(true)
            }
        }
        isLoading = false
    }

    //로그인/회원가입 판단 API 호출
    private fun checkLoginOrSignUp(context: Context, accessToken: String, refreshToken: String) {
        isLoading = true
        // 서버로부터 로그인/회원가입 상태를 확인 받음
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.userApiService.determineLoginOrSignUp(accessToken)
                handleLoginOrSignUpResponse(context, response, accessToken, refreshToken)
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
    private suspend fun handleLoginOrSignUpResponse(context: Context, response: Response<StatusResponse>, accessToken: String, refreshToken: String) {
        if (response.isSuccessful) {
            val statusResponse = response.body()
            if (statusResponse != null) {
                if(statusResponse.status == "login"){
                    loginToOurService(context, accessToken, refreshToken)
                }else if(statusResponse.status == "signup"){
                    println("이곳에 들어왔다, 회원가입이다")
                    tmpAccessToken = accessToken
                    tmpRefreshToken = refreshToken
                    println("액세스다"+tmpAccessToken)
                    println("리프레시다"+tmpRefreshToken)
                    
                    getKakaoUserInfo(context, tmpAccessToken?:""){ userInfo ->
                        if(userInfo != null){ //사용자 정보 불러오기 성공
                            println("유저 인포는 널이 아니다")
                            tmpkakaoUserInfo = userInfo
                            Log.i("SignUp", "사용자 정보: ${userInfo.name}")
                            loginStatus = statusResponse.status //회원가입 화면으로 이동
                        }else{
                            Log.e("SignUp", "사용자 정보 불러오기 실패")
                            Toast.makeText(context, "사용자 정보 불러오기 실패. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show()
                            //회원가입으로 넘어가지 못함
                        }
                    }
                }
            }
        } else {
            Log.e("LoginOrSignUp", "응답 실패: ${response.code()}")
        }
        isLoading = false
    }

    private suspend fun loginToOurService(context: Context, accessToken: String, refreshToken: String):Boolean {
        return try {
            val response = RetrofitInstance.userApiService.login(accessToken)
            println("이것은 response이다"+response)
            println("이곳에 흘러들어온 accessTOken은 다음과 같다: "+accessToken)
            if(response.isSuccessful && response.body()?.success == true){
//                val refreshToken = response.headers()["Set-cookie"] ?:"" //쿠키로 refreshToken 주기
                val expirationTime = System.currentTimeMillis() + 3600000  // 1시간 후 만료
                TokenManager.saveTokens(context, accessToken, refreshToken, expirationTime)
                isLoggedIn = true
                Log.i("Login", "우리 서비스 로그인 성공")

                //로그인 성공 후 사용자 정보 저장
                getKakaoUserInfo(context, accessToken) { userInfo ->
                    println("성공 후 저장하는 곳에 진입했다")
                    if (userInfo != null) {
                        println("진정한 성공이다, 사용자는 널이 아니다")
                        kakaoUserInfo = userInfo
                        Log.i("KakaoUserInfo", "사용자 정보: ${userInfo.name}")
                    } else {
                        println("이곳은 실패한 곳이다")
                        Log.e("KakaoUserInfo", "사용자 정보 가져오기 실패")
                    }
                }
                true
            } else {
                Log.e("Login", "우리 서비스 로그인 실패: ${response.code()}")
                false
            }
        } catch (e: Exception) {
            Log.e("Login", "우리 서비스 로그인 API 호출 실패", e)
            false
        }
    }


    // 카카오에서 사용자 정보를 가져오는 함수
    fun getKakaoUserInfo(context: Context, accessToken: String, callback: (KakaoUserInfo?) -> Unit) {
        isLoading = true
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.userApiService.getKakaoUserInfo(accessToken)
                if (response.isSuccessful) {
                    val userInfo = response.body()
                    if(userInfo != null){
                        Log.i("KakaoUserInfo", "사용자 정보: ${userInfo?.name}")
                        callback(userInfo)
                    } else {
                        Log.i("KakaoUserInfo", "사용자 정보가 null입니다.")
                        callback(null)
                    }
                } else {
                    Log.e("KakaoUserInfo", "응답 실패: ${response.code()}")
                    callback(null)
                }
            } catch (e: Exception) {
                Log.e("KakaoUserInfo", "API 호출 실패", e)
                callback(null)
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

                        // 바로 로그인 진행
                        CoroutineScope(Dispatchers.IO).launch {
                            val loginSuccess = loginToOurService(context, tmpAccessToken ?: "", tmpRefreshToken ?: "")
                            CoroutineScope(Dispatchers.Main).launch {
                                callback(loginSuccess) // 로그인 성공 여부 전달
                            }
                        }
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

    // 로그아웃
    fun logout(context: Context) {
        TokenManager.clearToken(context)
        isLoggedIn = false
    }


}