package com.example.gogoma.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gogoma.data.api.RetrofitInstance
import com.example.gogoma.data.model.CreateUserRequest
import com.example.gogoma.data.model.CreateUserResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class UserViewModel() : ViewModel() {

    // 회원가입 변수
    val createUserResponse = MutableLiveData<Response<CreateUserResponse>>()

    // 로그인 상태 관리
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> get() = _isLoggedIn

    // 회원가입 처리
    fun signup(request: CreateUserRequest) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.userApiService.signupKakao(request)
                createUserResponse.value = response
            } catch (e: Exception) {
                Log.e("UserViewModel", "Error: ${e.message}")
            }
        }
    }

}