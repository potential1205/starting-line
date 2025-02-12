package com.example.gogoma.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserMarathonSearchDto (
        val userMarathonId: Int,

        val marathonTitle: String,

        val marathonType: String,

        val dDay: String,

        val raceStartDateTime: String,

        val paymentDateTime: String
) : Parcelable