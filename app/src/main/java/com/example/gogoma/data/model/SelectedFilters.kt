package com.example.gogoma.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SelectedFilters (
    var marathonStatus: FilterItem? = null,
    var city: FilterItem? = null,
    var year: FilterItem? = null,
    var month: FilterItem? = null,
    var courseTypeList: List<FilterItem>? = emptyList(),
    var keyword: String? = null
) : Parcelable

sealed class FilterValue : Parcelable {
    @Parcelize
    data class StringValue(val value: String) : FilterValue()

    @Parcelize
    data class IntValue(val value: Int) : FilterValue()
}

@Parcelize
data class FilterItem(val displayText: String, val value: FilterValue) : Parcelable