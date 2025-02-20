package com.example.gogoma.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ApplyResponse(
    val name: String,
    val email: String,
    val gender: String,
    val birthMonth: String,
    val birthDay: String,
    val birthYear: String,
    val phoneNumber: String,
    val roadAddress: String,
    val detailAddress: String,
    val clothingSize: String
) : Parcelable
