package com.example.gogoma.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gogoma.GlobalApplication
import com.example.gogoma.data.api.RetrofitInstance
import com.example.gogoma.data.dto.UpdateUserMarathonRequest
import com.example.gogoma.data.model.FriendResponse
import com.example.gogoma.data.model.MarathonStartInitDataResponse
import com.example.gogoma.data.model.UpcomingMarathonInfoResponse
import com.example.gogoma.data.roomdb.entity.Friend
import com.example.gogoma.data.roomdb.entity.Marathon
import com.example.gogoma.data.roomdb.entity.MyInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PaceViewModel(private val globalApplication: GlobalApplication): ViewModel() {

    private val db = GlobalApplication.instance.database

    private val _marathonStartInitDataResponse = MutableStateFlow<MarathonStartInitDataResponse?>(null)
    val marathonStartInitDataResponse: StateFlow<MarathonStartInitDataResponse?> = _marathonStartInitDataResponse

    private val _upcomingMarathonInfoResponse = MutableStateFlow<UpcomingMarathonInfoResponse?>(null)
    val upcomingMarathonInfoResponse: StateFlow<UpcomingMarathonInfoResponse?> = _upcomingMarathonInfoResponse

    private val _friendList = MutableStateFlow<List<FriendResponse>>(emptyList())
    val friendList: StateFlow<List<FriendResponse>> = _friendList

    fun getInitData(accessToken: String, marathonId: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.watchApiService.getMarathonStartInitData(accessToken, marathonId)
                if (response.isSuccessful) {
                    _marathonStartInitDataResponse.value = response.body()
                    Log.d("marathon", _marathonStartInitDataResponse.value.toString())

                } else {
                    _marathonStartInitDataResponse.value = null
                    Log.e("marathon", "Marathon 데이터 불러오기 실패: ${response.code()}")
                }
            } catch (e: Exception) {
                _marathonStartInitDataResponse.value = null
                Log.e("API", "예외 발생: ${e.message}")
            }
        }
    }

    fun saveMarathonDataToDB(marathonData: MarathonStartInitDataResponse, onComplete: () -> Unit) {
        viewModelScope.launch {
            try {
                db.myInfoDao().clearMyInfo()
                db.marathonDao().clearMarathon()
                db.friendDao().clearAllFriends()

                db.myInfoDao().insertMyInfo(
                    MyInfo(
                        marathonData.userId,
                        marathonData.userName,
                        marathonData.targetPace,
                        marathonData.runningDistance
                    )
                )

                db.marathonDao().insertMarathon(
                    Marathon(
                        marathonData.marathonId,
                        marathonData.marathonTitle,
                        marathonData.marathonStartTime
                    )
                )

                marathonData.friendList.forEach { friend ->
                    db.friendDao().insertFriend(Friend(friend.userId, friend.friendName))
                }

                // 데이터 확인 로그
                Log.d("MyInfo", "[Room DB]" + db.myInfoDao().getMyInfo().toString())
                Log.d("Marathon","[Room DB]" + db.marathonDao().getMarathon().toString())
                Log.d("Friend", "[Room DB]" + db.friendDao().getAllFriends().toString())

                onComplete()

            } catch (e: Exception) {
                Log.e("Database", "Room DB 저장 실패: ${e.message}")
            }
        }
    }


    fun getUpcomingMarathonInfo(accessToken: String){
        viewModelScope.launch {
            try {
                val response =
                    RetrofitInstance.marathonApiService.getUpcomingMarathonInfo(accessToken)
                if (response.isSuccessful) {
                    _upcomingMarathonInfoResponse.value = response.body()
                } else {
                    _upcomingMarathonInfoResponse.value = null
                }
            } catch (e: Exception) {
                _upcomingMarathonInfoResponse.value = null
            }
        }
    }

    fun getUpcomingMarathonFriendList(accessToken: String){

        viewModelScope.launch {
            try {
                val response = RetrofitInstance.userApiService.getUpcomingMarathonFriendList(accessToken)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _friendList.value = it  // 응답 데이터를 StateFlow에 할당
                    }
                } else {
                    _friendList.value = emptyList()  // 실패 시 빈 리스트로 설정
                }
            } catch (e: Exception) {
                _friendList.value = emptyList()  // 예외 발생 시 빈 리스트로 설정
                Log.e("API", "getUpcomingMarathonFriendList 예외 발생: ${e.message}")
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