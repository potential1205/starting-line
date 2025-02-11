// Repository.kt
package com.example.gogoma.data.roomdb.repository

import com.example.gogoma.data.roomdb.entity.Friend
import com.example.gogoma.data.roomdb.entity.Marathon
import com.example.gogoma.data.roomdb.entity.MyInfo
import com.example.newroom.AppDatabase
import kotlinx.coroutines.runBlocking

class RoomRepository(private val db: AppDatabase) {

    suspend fun updateDataFromServer(
        newMeList: List<MyInfo>,
        newFriendList: List<Friend>,
        newMarathonList: List<Marathon>
    ) {
        db.runInTransaction {
            newMeList.forEach { runBlocking { db.myInfoDao().insertMyInfo(it) } }
            newFriendList.forEach { runBlocking { db.friendDao().insertFriend(it) } }
            newMarathonList.forEach { runBlocking { db.marathonDao().insertMarathon(it) } }
        }
    }

    suspend fun resetDatabase() {
        db.clearAllTables()
    }

    suspend fun getMyInfo(): MyInfo? {
        // 만약 MyInfo가 항상 하나만 저장된다면
        return db.myInfoDao().getMyInfo()
    }

    suspend fun getAllFriends(): List<Friend> {
        return db.friendDao().getAllFriends()
    }

    suspend fun getAllMarathons(): Marathon? {
        return db.marathonDao().getMarathon()
    }

}
