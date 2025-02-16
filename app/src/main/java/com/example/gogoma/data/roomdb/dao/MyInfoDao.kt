package com.example.gogoma.data.roomdb.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gogoma.data.roomdb.entity.MyInfo

@Dao
interface MyInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMyInfo(myInfo: MyInfo)

    @Query("SELECT * FROM MyInfo LIMIT 1")
    suspend fun getMyInfo(): MyInfo?

    @Query("DELETE FROM MyInfo")
    suspend fun clearMyInfo()
}
