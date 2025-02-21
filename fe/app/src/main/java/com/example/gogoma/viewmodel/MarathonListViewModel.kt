package com.example.gogoma.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gogoma.data.api.RetrofitInstance
import com.example.gogoma.data.model.MarathonPreviewDto
import com.example.gogoma.data.model.MarathonSearchResponse
import kotlinx.coroutines.launch

class MarathonListViewModel : ViewModel() {
    var marathonSearchResponseList by mutableStateOf<List<MarathonPreviewDto>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    // 필터링 값
    var marathonStatus by mutableStateOf<String?>(null) // OPEN, CLOSED, FINISHED
    var city by mutableStateOf<String?>(null)
    var year by mutableStateOf<String?>(null)
    var month by mutableStateOf<String?>(null)
    var courseTypeList by mutableStateOf<List<String>?>(null)
    var keyword by mutableStateOf<String?>(null)

    init {
        loadMarathons()
    }

    private fun loadMarathons() {
        isLoading = true
        errorMessage = null
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.apiService.getMarathons(
                    marathonStatus,
                    city,
                    year,
                    month,
                    courseTypeList,
                    keyword
                )
                if (response.isSuccessful) {
                    marathonSearchResponseList = response.body()?.marathonPreviewDtoList ?: emptyList()
                } else {
                    errorMessage = "Failed to load data: ${response.code()}"
                }
            } catch (e: Exception) {
                errorMessage = "Error: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    // 필터값을 갱신하고, 다시 API 호출하는 함수
    fun updateFilters(
        marathonStatus: String?,
        city: String?,
        year: String?,
        month: String?,
        courseTypeList: List<String>?,
        keyword: String?
    ) {
        this.marathonStatus = marathonStatus
        this.city = city
        this.year = year
        this.month = month
        this.courseTypeList = courseTypeList
        this.keyword = keyword
        loadMarathons() // 필터를 적용하여 마라톤 목록 갱신
    }
}