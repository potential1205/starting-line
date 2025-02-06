package com.example.gogoma.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Friend (
    val rank: Int,
    val friendId : Int,
    val name : String,
    val profileImage : String?,
    val totalDistance: Double
) : Parcelable