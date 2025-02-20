package com.example.gogoma.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FriendListResponse(
    val userId: Int,
    val friendResponseList: List<FriendResponse>
): Parcelable
