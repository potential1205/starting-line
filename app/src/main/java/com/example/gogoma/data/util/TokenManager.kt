package com.example.gogoma.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.gogoma.data.api.UserApiService
import com.example.gogoma.data.model.KakaoClientOauthTokenResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

object TokenManager {
    private const val PREF_NAME = "gogoma_prefs" // SharedPreferences의 파일명
    private const val KEY_ACCESS_TOKEN  = "access_token"
    private const val KEY_REFRESH_TOKEN = "refresh_token"
    private const val KEY_EXPIRATION_TIME = "expiration_time" // 만료 시간 저장 키값

    // SharedPreferences 객체 가져오기
    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    // 저장된 Access Token 가져오기
    fun getAccessToken(context: Context): String? {
        return getSharedPreferences(context).getString(KEY_ACCESS_TOKEN, null)
    }

    // 저장된 Refresh Token 가져오기
    fun getRefreshToken(context: Context): String? {
        return getSharedPreferences(context).getString(KEY_REFRESH_TOKEN, null)
    }

    // Access Token과 만료 시간을 SharedPreferences에 저장
    fun saveAccessToken(context: Context, accessToken: String, expirationTime: Long) {
        getSharedPreferences(context).edit().apply {
            putString(KEY_ACCESS_TOKEN, accessToken)
            putLong(KEY_EXPIRATION_TIME, expirationTime) //만료 시간 저장
            apply()
        }
    }

    // Refresh Token을 SharedPreferences에 저장
    private fun saveRefreshToken(context: Context, refreshToken: String) {
        getSharedPreferences(context).edit().apply {
            putString(KEY_REFRESH_TOKEN, refreshToken)
            apply()
        }
    }

    // 모든 토큰 및 만료 시간을 삭제
    fun clearToken(context: Context) {
        getSharedPreferences(context).edit().apply {
            remove(KEY_ACCESS_TOKEN)
            remove(KEY_REFRESH_TOKEN)
            remove(KEY_EXPIRATION_TIME)
            apply()
        }
    }

    // 저장된 토큰 만료 시간 가져오기
    private fun getExpirationTime(context: Context): Long {
        return getSharedPreferences(context).getLong(KEY_EXPIRATION_TIME, -1L)
    }

    // Token이 만료되었는지 확인 : 유효하면 false, 만료됐으면 true 반환
    fun isTokenExpired(context: Context): Boolean {
        val expirationTime = getExpirationTime(context)
        return expirationTime == -1L || System.currentTimeMillis() > expirationTime // 만료 시간이 없거나 현재 시간이 더 크면 만료된 것으로 판단
    }

    // 토큰이 만료되었으면 새로 갱신, 토큰 갱신 시 호출된 콜백
    suspend fun refreshAccessTokenIfNeeded(context: Context, apiService: UserApiService, callback: (Boolean) -> Unit) {
        val token = getAccessToken(context)

        // 토큰이 없거나 만료되었으면 refresh token으로 갱신
        if (token == null || isTokenExpired(context)) {
            val refreshToken = getRefreshToken(context)
            if (refreshToken != null) {
                // Refresh Token을 이용해서 Access Token을 갱신
                return withContext(Dispatchers.IO) {
                    try {
                        val response: Response<KakaoClientOauthTokenResponse> = apiService.refreshAccessToken(refreshToken)
                        if (response.isSuccessful) {
                            response.body()?.let {
                                val newToken = it.access_token
                                val newRefreshToken = it.refresh_token
                                val expirationTime = System.currentTimeMillis() + 3600000 // 만료 시간 1시간 후
                                saveAccessToken(context, newToken, expirationTime)
                                saveRefreshToken(context, newRefreshToken)
                                callback(true)
                                return@withContext
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    callback(false)
                }
            }
        }
        callback(false)
    }

    // expirationTime 저장 함수
    fun saveExpirationTime(context: Context, expirationTime: Long) {
        val sharedPreferences = context.getSharedPreferences("tokens", Context.MODE_PRIVATE)
        sharedPreferences.edit().putLong("expiration_time", expirationTime).apply()
    }

    // 로그인 성공 시 Access Token과 Refresh Token을 저장
    fun saveTokens(context: Context, accessToken: String, refreshToken: String, expirationTime: Long) {
        saveAccessToken(context, accessToken, expirationTime)
        saveRefreshToken(context, refreshToken)
        saveExpirationTime(context, expirationTime)
    }
}
