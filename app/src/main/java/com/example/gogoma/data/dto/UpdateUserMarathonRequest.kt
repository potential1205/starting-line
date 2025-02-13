package com.example.gogoma.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UpdateUserMarathonRequest(
    val targetPace: Int
) : Parcelable
