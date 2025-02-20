package com.example.gogoma.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ApplyInfoRequest(
    val roadAddress: String?,
    val detailAddress: String?,
    val clothingSize: String?
) : Parcelable