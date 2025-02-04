package com.example.gogoma.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CreateUserRequest (
    val kakaoId: Long,
    val name: String,
    val profileImage: String,
    val email: String,
    val gender: String, // 원래 Enum이지만, 여기서는 string 처리
    val birthDate: String,
    val birthYear: String,
    val phoneNumber: String,
    val roadAddress: String,
    val detailAddress: String,
    val clothingSize: String
) : Parcelable