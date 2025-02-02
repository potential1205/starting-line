package com.example.gogoma.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MarathonDetailResponse(
    val marathon: Marathon,
    val marathonTypeList: List<MarathonType>
) : Parcelable