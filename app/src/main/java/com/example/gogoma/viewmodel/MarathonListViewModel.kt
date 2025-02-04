package com.example.gogoma.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gogoma.data.api.RetrofitInstance
import com.example.gogoma.data.model.MarathonPreviewDto
import com.example.gogoma.data.model.MarathonSearchResponse
import com.example.gogoma.data.model.SelectedFilters
import kotlinx.coroutines.launch

class MarathonListViewModel : ViewModel() {
    var selectedFilters by mutableStateOf(SelectedFilters())
    var pendingFilters by mutableStateOf(SelectedFilters()) //임시 저장 변수

    //마라톤 리스트 내용
    var marathonSearchResponseList by mutableStateOf<List<MarathonPreviewDto>>(emptyList())
        private set
    var cityList by mutableStateOf<List<String>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    //필터 내용
    val filterTitles = listOf(
        "지역",
        "접수 상태",
        "종목",
        "년도",
        "월"
    )

    val filterContents: Map<String, List<String>>
        get() = mapOf(
            "지역" to cityList,  // 동적으로 cityList를 반영
            "접수 상태" to listOf("OPEN", "CLOSED", "FINISHED"),
            "종목" to listOf("마라톤", "하프마라톤", "10km", "5km"),
            "년도" to (2025 downTo 2000).map { "${it}년" },
            "월" to (1..12).map { "${it}월" }
        )

    init {
        loadMarathons()
    }

    private fun loadMarathons() {
        isLoading = true
        errorMessage = null
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.apiService.getMarathons(
                    selectedFilters.marathonStatus,
                    selectedFilters.city,
                    selectedFilters.year,
                    selectedFilters.month,
                    selectedFilters.courseTypeList,
                    selectedFilters.keyword
                )
                if (response.isSuccessful) {
                    marathonSearchResponseList = response.body()?.marathonPreviewDtoList ?: emptyList()
                    cityList = response.body()?.cityList ?: emptyList()
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
        marathonStatus: String? = selectedFilters.marathonStatus,
        city: String? = selectedFilters.city,
        year: String? = selectedFilters.year,
        month: String? = selectedFilters.month,
        courseTypeList: List<String>? = selectedFilters.courseTypeList,
        keyword: String? = selectedFilters.keyword
    ) {
        selectedFilters = SelectedFilters(
            marathonStatus = marathonStatus,
            city = city,
            year = year,
            month = month,
            courseTypeList = courseTypeList,
            keyword = keyword
        )
        println("필터 업데이트됨: marathonStatus=$marathonStatus, city=$city, year=$year, month=$month, courseTypeList=$courseTypeList, keyword=$keyword")
        loadMarathons() // 필터를 적용하여 마라톤 목록 갱신
    }

    fun updatePendingFilter(
        city: String? = null,
        marathonStatus: String? = null,
        year: String? = null,
        month: String? = null,
        courseTypeList: List<String>? = null
    ) {
        pendingFilters = SelectedFilters(
            marathonStatus = marathonStatus,
            city = city,
            year = year,
            month = month,
            courseTypeList = courseTypeList,
            keyword = pendingFilters.keyword //keyword 유지-확장 고려
        )
    }

    //전체 필터 모달창에서 대회 보기 버튼 로직
    fun applyFilters(){
        selectedFilters = pendingFilters  // 임시 필터 값을 실제 필터로 적용
        loadMarathons()  // 필터를 적용하여 마라톤 목록 갱신
    }
}