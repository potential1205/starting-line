package com.example.gogoma

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.room.Room
import com.example.gogoma.data.roomdb.repository.RoomRepository
import com.example.gogoma.theme.GogomaTheme
import com.example.gogoma.ui.navigation.AppNavigation
import com.example.gogoma.viewmodel.UserViewModel
import com.example.newroom.AppDatabase
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class MainActivity : ComponentActivity() {
    private val userViewModel: UserViewModel by viewModels()
    private lateinit var db: AppDatabase
    private lateinit var repository: RoomRepository
    private val TAG = "OAuthRedirectActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        userViewModel.init(applicationContext)
        setContent {
            GogomaApp(userViewModel)
        }
        db = Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            "test-database"
        )
            .fallbackToDestructiveMigration()
            .build()

        repository = RoomRepository(db)

        //첫 실행 시 Intent 처리
        handleIntent(intent)
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
}

@Composable
fun GogomaApp(userViewModel: UserViewModel){
    GogomaTheme {
        AppNavigation(userViewModel)
    }
}
