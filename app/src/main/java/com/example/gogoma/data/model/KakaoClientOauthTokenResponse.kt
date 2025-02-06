package com.example.gogoma.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class KakaoClientOauthTokenResponse (
    val status: String,
    val access_token: String,
    val refresh_token: String
) : Parcelable