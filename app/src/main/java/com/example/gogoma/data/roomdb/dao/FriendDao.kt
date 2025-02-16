package com.example.gogoma.data.roomdb.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gogoma.data.roomdb.entity.Friend

@Dao
interface FriendDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFriend(friend: Friend)  // 하나씩 삽입

    @Query("SELECT * FROM Friend")
    fun getAllFriends(): LiveData<List<Friend>>  // LiveData로 모든 친구 반환

    @Query("DELETE FROM Friend")
    fun clearFriends()
}
