package com.example.newroom.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.gogoma.data.roomdb.entity.Friend

@Dao
interface FriendDao {
    @Insert
    fun insertFriend(friend: Friend)

    @Query("SELECT * FROM Friend")
    fun getAllFriends(): List<Friend>
}
