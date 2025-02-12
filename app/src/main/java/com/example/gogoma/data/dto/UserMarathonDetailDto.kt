package com.example.gogoma.data.dto

import android.os.Parcelable
import com.example.gogoma.data.model.PaymentType
import com.example.gogoma.data.model.Marathon
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserMarathonDetailDto (
    val marathon: Marathon,

    val courseTypeList: List<String>,

    val paymentType: PaymentType,

    val paymentAmount: String,

    val paymentDateTime: String,

    val address: String,

    val selectedCourseType: String
) : Parcelable