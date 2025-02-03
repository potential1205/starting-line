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
    //필터 내용
    val filterTitles = listOf(
        "지역",
        "접수 상태",
        "종목",
        "년도",
        "월"
    )

    val filterContents = mapOf(
        "지역" to listOf("서울특별시", "경기도", "부산광역시"),
        "접수 상태" to listOf("접수중", "마감됨"),
        "종목" to listOf("마라톤", "하프마라톤", "10km", "5km"),
        "년도" to (2025 downTo 2000).map { "${it}년" },
        "월" to (1..12).map { "${it}월" }
    )

    var marathonStatus by mutableStateOf<String?>(null) // OPEN, CLOSED, FINISHED
    var city by mutableStateOf<String?>(null)
    var year by mutableStateOf<String?>(null)
    var month by mutableStateOf<String?>(null)
    var courseTypeList by mutableStateOf<List<String>?>(null)
    var keyword by mutableStateOf<String?>(null)

    //마라톤 리스트 내용
//    var marathonSearchResponseList by mutableStateOf<List<MarathonPreviewDto>>(emptyList())
//        private set

    private val _marathonSearchResponseList = mutableStateOf<List<MarathonPreviewDto>>(emptyList())
    val marathonSearchResponseList: List<MarathonPreviewDto> by _marathonSearchResponseList

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

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
                    _marathonSearchResponseList.value = response.body()?.marathonPreviewDtoList ?: emptyList()
                    println(_marathonSearchResponseList)
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
        marathonStatus: String? = this.marathonStatus,
        city: String? = this.city,
        year: String? = this.year,
        month: String? = this.month,
        courseTypeList: List<String>? = this.courseTypeList,
        keyword: String? = this.keyword
    ) {
        this.marathonStatus = marathonStatus
        this.city = city
        this.year = year
        this.month = month
        this.courseTypeList = courseTypeList
        this.keyword = keyword
        println("필터 업데이트됨: marathonStatus=$marathonStatus, city=$city, year=$year, month=$month, courseTypeList=$courseTypeList, keyword=$keyword")
        loadMarathons() // 필터를 적용하여 마라톤 목록 갱신
    }
}