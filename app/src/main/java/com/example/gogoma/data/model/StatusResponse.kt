package com.example.gogoma.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StatusResponse (
    val status: String
) : Parcelable