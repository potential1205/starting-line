package com.example.gogoma.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MarathonType(
    val id: Int,
    val marathonId: Int,
    val courseType: Int,
    val price: String,
    val etc: String
) : Parcelable

fun MarathonType.getCourseTypeInKm(): String {
    val kmValue = courseType / 100000.0 // cm를 km로 변환
    return if (kmValue % 1 == 0.0) {
        "${kmValue.toInt()}km"
    } else {
        "%.3fkm".format(kmValue)
    }
}