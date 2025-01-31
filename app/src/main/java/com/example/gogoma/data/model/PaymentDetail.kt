package com.example.gogoma.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PaymentDetail (
    val paymentDate: String,
    val paymentType: String,
    val paymentAmount: String,
    val address: String,
    val raceCategory: String,
    val gift: String
) : Parcelable