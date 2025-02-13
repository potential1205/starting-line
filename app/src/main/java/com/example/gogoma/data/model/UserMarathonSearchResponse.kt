package com.example.gogoma.data.model

import android.os.Parcelable
import com.example.gogoma.data.dto.UserMarathonSearchDto
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserMarathonSearchResponse(
    val userMarathonSearchDtoList: List<UserMarathonSearchDto>
) : Parcelable
