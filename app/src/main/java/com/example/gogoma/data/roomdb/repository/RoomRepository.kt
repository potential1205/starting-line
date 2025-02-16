// Repository.kt
package com.example.gogoma.data.roomdb.repository

import androidx.lifecycle.LiveData
import com.example.gogoma.data.roomdb.entity.Friend
import com.example.gogoma.data.roomdb.entity.Marathon
import com.example.gogoma.data.roomdb.entity.MyInfo
import com.example.newroom.AppDatabase
import kotlinx.coroutines.runBlocking

class RoomRepository(private val db: AppDatabase) {

    fun getMyInfo(): LiveData<MyInfo> = db.myInfoDao().getMyInfo()
    fun getMarathon(): LiveData<Marathon> = db.marathonDao().getMarathon()
    fun getAllFriends(): LiveData<List<Friend>> = db.friendDao().getAllFriends()

    fun saveMyInfo(myInfo: MyInfo) {
        db.runInTransaction {
            db.myInfoDao().clearMyInfo()
            db.myInfoDao().insertMyInfo(myInfo)
        }
    }

    fun saveMarathon(marathon: Marathon) {
        db.runInTransaction {
            db.marathonDao().clearMarathon()
            db.marathonDao().insertMarathon(marathon)
        }
    }

    fun saveFriend(friend: Friend) {  // Friend 한 명씩 저장
        db.runInTransaction {
            db.friendDao().insertFriend(friend)
        }
    }
}

