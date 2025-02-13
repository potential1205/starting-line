package com.example.gogoma.data.model

import android.os.Parcelable
import com.example.gogoma.data.dto.UserMarathonDetailDto
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserMarathonDetailResponse (
    var userMarathonDetail: UserMarathonDetailDto
) : Parcelable