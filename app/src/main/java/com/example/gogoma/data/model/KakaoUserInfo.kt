package com.example.gogoma.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class KakaoUserInfo (
    val profileImage: String,
    val email: String,
    val name: String,
    val gender: String,    // 예: "MALE" 또는 "FEMALE"
    val birthDate: String, // 예: "MM-DD"
    val birthYear: String, // 예: "YYYY"
    val phoneNumber: String,
    val id: Long
) : Parcelable