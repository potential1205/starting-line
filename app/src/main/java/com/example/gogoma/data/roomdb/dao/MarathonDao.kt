package com.example.newroom.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.gogoma.data.roomdb.entity.Marathon

@Dao
interface MarathonDao {
    @Insert
    suspend fun insertMarathon(marathon: Marathon)

    @Query("SELECT * FROM Marathon limit 1")
    suspend fun getMarathon(): Marathon?
}
