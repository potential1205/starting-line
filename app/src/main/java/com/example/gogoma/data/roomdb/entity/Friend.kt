package com.example.gogoma.data.roomdb.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Friend(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val name: String
)
