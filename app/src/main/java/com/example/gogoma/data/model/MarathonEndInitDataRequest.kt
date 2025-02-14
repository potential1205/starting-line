package com.example.gogoma.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MarathonEndInitDataRequest(
    val currentPace: Int,
    val runningTime: Int,
    val totalMemberCount: Int,
    val myRank: Int
) : Parcelable