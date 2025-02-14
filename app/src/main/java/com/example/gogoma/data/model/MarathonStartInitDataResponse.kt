package com.example.gogoma.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MarathonStartInitDataResponse(
    val userId: Int,
    val marathonId: Int,
    val userName: String,
    val targetPace: Int,
    val runningDistance: Int,
    val marathonTitle: String,
    val marathonStartTime: String,
    val friendList: List<Friend>
) : Parcelable