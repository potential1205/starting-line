package com.example.gogoma.data.roomdb.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gogoma.data.roomdb.entity.Friend

@Dao
interface FriendDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFriend(friend: Friend)

    @Query("SELECT * FROM Friend")
    suspend fun getAllFriends(): List<Friend>

    @Query("DELETE FROM Friend WHERE id = :friendId")
    suspend fun deleteFriend(friendId: Int)

    @Query("DELETE FROM Friend")
    suspend fun clearAllFriends()
}

