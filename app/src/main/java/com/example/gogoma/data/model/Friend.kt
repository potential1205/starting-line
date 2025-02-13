package com.example.gogoma.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Friend(
    val id: Int,
    val userId: Int,
    val friendId: Int,
    val friendName: String
):Parcelable
