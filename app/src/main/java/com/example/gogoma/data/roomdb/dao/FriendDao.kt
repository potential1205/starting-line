package com.example.newroom.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.gogoma.data.roomdb.entity.Friend

@Dao
interface FriendDao {
    @Insert
    suspend fun insertFriend(friend: Friend)

    @Query("SELECT * FROM Friend")
    suspend fun getAllFriends(): List<Friend>
}
