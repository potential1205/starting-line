package com.example.gogoma.data.roomdb.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class Marathon(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val name: String,
    val startTime: String
)
