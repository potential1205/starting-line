package com.example.gogoma.data.roomdb.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gogoma.data.roomdb.entity.Marathon

@Dao
interface MarathonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMarathon(marathon: Marathon)

    @Query("SELECT * FROM Marathon LIMIT 1")
    suspend fun getMarathon(): Marathon?

    @Query("DELETE FROM Marathon")
    suspend fun clearMarathon()
}


