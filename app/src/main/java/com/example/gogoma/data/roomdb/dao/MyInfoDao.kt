package com.example.newroom.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.gogoma.data.roomdb.entity.MyInfo

@Dao
interface MyInfoDao {
    @Insert
    suspend fun insertMyInfo(myInfo: MyInfo)

    @Query("SELECT * FROM MyInfo limit 1 ")
    suspend fun getMyInfo(): MyInfo?
}
