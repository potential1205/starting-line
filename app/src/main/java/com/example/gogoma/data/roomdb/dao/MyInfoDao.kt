package com.example.gogoma.data.roomdb.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gogoma.data.roomdb.entity.MyInfo

@Dao
interface MyInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMyInfo(myInfo: MyInfo)

    @Query("SELECT * FROM MyInfo LIMIT 1")
    fun getMyInfo(): LiveData<MyInfo>  // LiveData 반환

    @Query("DELETE FROM MyInfo")
    fun clearMyInfo()
}
