package com.example.newroom.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.gogoma.data.roomdb.entity.Marathon

@Dao
interface MarathonDao {
    @Insert
    fun insertMarathon(marathon: Marathon)

    @Query("SELECT * FROM Marathon limit 1")
    fun getMarathon(): Marathon?
}
