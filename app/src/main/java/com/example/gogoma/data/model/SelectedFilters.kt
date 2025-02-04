package com.example.gogoma.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SelectedFilters (
    var marathonStatus: String? = null,
    var city: String? = null,
    var year: String? = null,
    var month: String? = null,
    var courseTypeList: List<String>? = null,
    var keyword: String? = null
) : Parcelable