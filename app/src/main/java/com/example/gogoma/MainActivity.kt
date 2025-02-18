package com.example.gogoma

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import com.example.gogoma.theme.GogomaTheme
import com.example.gogoma.ui.navigation.AppNavigation
import com.example.gogoma.viewmodel.UserViewModel
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher

import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext

class MainActivity : ComponentActivity() {
    private val userViewModel: UserViewModel by viewModels()
    private val TAG = "OAuthRedirectActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        userViewModel.init(applicationContext)
        setContent {
            GogomaApp(userViewModel)
        }
        createNotificationChannel()
        //첫 실행 시 Intent 처리
        handleIntent(intent)

        lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

        // Activity 시작 시 위치 권한 요청을 위한 launcher 등록
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true &&
                    permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
                    // 권한이 허용되었으므로 필요한 작업 수행 (예: MarathonRunService 실행 전 미리 처리)
                    // 필요에 따라 여기서 추가 작업을 할 수 있습니다.
                } else {
                    Log.e("MarathonRunService", "위치 권한이 거부되었습니다.")

                }
            }

        // 앱 시작 시 즉시 위치 권한 요청
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
        // Android 13 이상에서 알림 권한(POST_NOTIFICATIONS) 요청
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val notificationPermissionLauncher = registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted ->
                if (isGranted) {
                    Log.d(TAG, "알림 권한 허용됨")
                } else {
                    Log.e(TAG, "알림 권한 거부됨")
                    Toast.makeText(this, "푸시 알림을 받으려면 알림 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
                }
            }
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        //이미 실행된 상태에서의 Intent 처리
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        intent?.data?.let { uri ->
            if(uri.scheme == "gogoma" && uri.host == "oauth") {
                val code = uri.getQueryParameter("code") //인증 코드
                Log.d(TAG, "auth code: ${code}")
                if(code != null) {
                    requestAccessToken(code)
                }
            }
        }
    }

    private fun requestAccessToken(code: String){
        val client = OkHttpClient()

        val requestBody = FormBody.Builder()
            .add("grant_type", "authorization_code")
            .add("client_id", BuildConfig.CLIENT_ID)
            .add("redirect_uri", BuildConfig.REDIRECT_URI)
            .add("code", code)
            .build()

        val request = Request.Builder()
            .url("https://kauth.kakao.com/oauth/token")
            .post(requestBody)
            .build()

        //비동기 실행 요청
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, "Token request failed: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful) {
                    val responseBody = response.body?.string()

                    //응답 본문 출력
                    Log.d(TAG, "response: ${response}")

                    // 응답 본문에서 액세스 토큰 추출 (JSON 파싱)
                    try {
                        val jsonResponse = JSONObject(responseBody)
                        val accessToken = jsonResponse.getString("access_token")
                        val refreshToken = jsonResponse.getString("refresh_token")
                        Log.d(TAG, "Access Token: $accessToken")
                        Log.d(TAG, "Refresh Token: $refreshToken")

                        // ViewModel로 토큰을 전달하여 후속 처리 진행
                        if(accessToken != null && refreshToken != null) {
                            userViewModel.checkLoginOrSignUp(applicationContext, accessToken, refreshToken)
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Failed to parse access token: ${e.message}")
                    }
                } else {
                    Log.e(TAG, "Token request failed: ${response.code}")
                }
            }
        })
    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "my_channel",
                "대회 알림",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "대회 신청 관련 알림"
            }

            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }
}

@Composable
fun GogomaApp(userViewModel: UserViewModel){
    GogomaTheme {
        AppNavigation(userViewModel)
    }
}
