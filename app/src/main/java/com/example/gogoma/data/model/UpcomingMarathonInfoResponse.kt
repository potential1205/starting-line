package com.example.gogoma.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UpcomingMarathonInfoResponse(
    var marathon: Marathon,
    var end : Boolean
) : Parcelable
