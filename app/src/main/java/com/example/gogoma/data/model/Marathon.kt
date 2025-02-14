package com.example.gogoma.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Marathon(
    val id: Int,
    val title: String,
    val registrationStartDateTime: String,
    val registrationEndDateTime: String,
    val raceStartTime: String,
    val accountBank: String,
    val accountNumber: String,
    val accountName: String,
    val location: String,
    val city: String,
    val year: String,
    val month: String,
    val region: String,
    val district: String,
    val hostList: List<String>,
    val organizerList: List<String>,
    val sponsorList: List<String>,
    val qualifications: String,
    val marathonStatus: String,
    val viewCount: Int,
    val thumbnailImage: String,
    val infoImage: String,
    val courseImage: String,
    val formType: Int,
    val formUrl: String,
    val homeUrl: String
) : Parcelable