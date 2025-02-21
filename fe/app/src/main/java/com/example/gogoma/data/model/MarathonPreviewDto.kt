package com.example.gogoma.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MarathonPreviewDto (
    val id: Int,
    val title: String,
    val registrationStartDateTime: String,
    val registrationEndDateTime: String,
    val raceStartTime: String,
    val location: String,
    val city: String,
    val region: String,
    val district: String,
    val marathonStatus: String,
    val thumbnailImage: String,
    val courseTypeList: List<String>,
    val marathonTypeList: List<MarathonType>,
    val dday: String
) : Parcelable