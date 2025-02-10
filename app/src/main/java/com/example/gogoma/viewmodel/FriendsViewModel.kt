package com.example.gogoma.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.network.HttpException
import com.example.gogoma.data.api.RetrofitInstance
import com.example.gogoma.data.model.Friend
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okio.IOException

class FriendsViewModel : ViewModel() {
    private val _friends = MutableStateFlow<List<Friend>>(emptyList())
    val friends: StateFlow<List<Friend>> = _friends

    private val _errorMessage = MutableSharedFlow<String?>()
    val errorMessage: SharedFlow<String?> = _errorMessage

    fun fetchFriends(accessToken: String?) {
        viewModelScope.launch {
            try {
                val friends = RetrofitInstance.friendApiService.getFriends(accessToken)
                _friends.value = friends
            } catch (e: HttpException) {
                _errorMessage.emit("HTTP 오류 발생: ${e.message}")
            } catch (e: IOException) {
                _errorMessage.emit("네트워크 오류 발생")
            } catch (e: Exception) {
                _errorMessage.emit(e.message)
            }
        }
    }

    // m → km 변환
    private fun Friend.toKm(): Friend {
        return this.copy(totalDistance = this.totalDistance / 1000)
    }
}