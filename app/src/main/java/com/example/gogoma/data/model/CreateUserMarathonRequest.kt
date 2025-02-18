package com.example.gogoma.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CreateUserMarathonRequest(
    val marathonId: Int,
    val address: String,
    val paymentType: PaymentType, // KAKAO_PAY, BANK_DEPOSIT
    val paymentAmount: String,
    val paymentDateTime: String,
    val courseType: Int
) : Parcelable
