package com.example.gogoma.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Address(
    val id: String,
    val name: String,
    val address: String,         // 기본 주소
    val detailAddress: String,   // 상세 주소
    val phone: String,
    val isDefault: Boolean = false
) : Parcelable
