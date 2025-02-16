package com.example.gogoma.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gogoma.GlobalApplication
import com.example.gogoma.data.api.RetrofitInstance
import com.example.gogoma.data.dto.UpdateUserMarathonRequest
import com.example.gogoma.data.model.MarathonStartInitDataResponse
import com.example.gogoma.data.model.UpcomingMarathonInfoResponse
import com.example.gogoma.data.roomdb.entity.Friend
import com.example.gogoma.data.roomdb.entity.Marathon
import com.example.gogoma.data.roomdb.entity.MyInfo
import kotlinx.coroutines.launch

class PaceViewModel(private val globalApplication: GlobalApplication): ViewModel() {

    private val db = GlobalApplication.instance.database

    var marathonStartInitDataResponse by mutableStateOf<MarathonStartInitDataResponse?>(null)
        private set

    var upcomingMarathonInfoResponse by mutableStateOf<UpcomingMarathonInfoResponse?>(null)
        private set


    fun getInitData(accessToken: String, marathonId: Int) {
        viewModelScope.launch {
            try {
                // RetrofitInstance를 사용하여 API 호출
                val response = RetrofitInstance.watchApiService.getMarathonStartInitData(accessToken,marathonId)
                if (response.isSuccessful) {
                    marathonStartInitDataResponse = response.body()
                        db.myInfoDao().clearMyInfo()
                        db.marathonDao().clearMarathon()
                        db.friendDao().clearAllFriends()

                        db.myInfoDao().insertMyInfo(
                            MyInfo(
                                marathonStartInitDataResponse!!.userId,
                                marathonStartInitDataResponse!!.userName,
                                marathonStartInitDataResponse!!.targetPace
                            )
                        )
                        Log.d("Database", "MyInfo 저장 성공")

                        db.marathonDao().insertMarathon(
                            Marathon(
                                marathonStartInitDataResponse!!.marathonId,
                                marathonStartInitDataResponse!!.marathonTitle,
                                marathonStartInitDataResponse!!.marathonStartTime
                            )
                        )
                        Log.d("Database", "Marathon 저장 성공")

                    marathonStartInitDataResponse!!.friendList.forEach { friend ->
                            db.friendDao().insertFriend(Friend(friend.friendId, friend.friendName))
                            Log.d("Database", "Friend 저장 성공: ${friend.friendName}")
                    }

                    Log.d("MyInfo",db.myInfoDao().getMyInfo().toString())
                    Log.d("Marathon",db.marathonDao().getMarathon().toString())
                    Log.d("Friend",db.friendDao().getAllFriends().toString())
                    //globalApplication.initData = marathonStartInitDataResponse
                } else {
                    // 실패 시 처리 (예: 로그 찍기, 에러 메시지 출력 등)
                    marathonStartInitDataResponse = null
                }
            } catch (e: Exception) {
                // 예외 처리 (예: 네트워크 에러 등)
                marathonStartInitDataResponse = null
            }
        }
    }

    fun getUpcomingMarathonInfo(accessToken: String){
        viewModelScope.launch {
            try {
                val response =
                    RetrofitInstance.marathonApiService.getUpcomingMarathonInfo(accessToken)
                if (response.isSuccessful) {
                    upcomingMarathonInfoResponse = response.body()
                } else {
                    upcomingMarathonInfoResponse = null
                }
            } catch (e: Exception) {
                upcomingMarathonInfoResponse = null
            }
        }
    }

    fun patchMarathonPace(accessToken: String, marathonId: Int, targetPace: Int) {
        val request = UpdateUserMarathonRequest(targetPace)

        viewModelScope.launch {
            try {
                val response = RetrofitInstance.userMarathonApiService.updateUserMarathon(accessToken, marathonId, request)
                if (response.success) {
                    // 성공 시 갱신
                    getInitData(accessToken, marathonId)
                } else {
                    // 실패
                }
            } catch (e: Exception) {
                // 네트워크 오류
            }
        }
    }


}