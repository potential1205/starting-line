package com.example.gogoma.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MarathonType(
    val id: Int,
    val marathonId: Int,
    val courseType: String,
    val price: String,
    val etc: String
) : Parcelable