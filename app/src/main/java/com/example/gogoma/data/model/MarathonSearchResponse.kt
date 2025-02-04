package com.example.gogoma.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MarathonSearchResponse(
    val marathonPreviewDtoList: List<MarathonPreviewDto>,
    val cityList: List<String>
) : Parcelable
