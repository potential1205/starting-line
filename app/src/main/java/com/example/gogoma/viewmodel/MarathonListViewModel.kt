package com.example.gogoma.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gogoma.data.api.RetrofitInstance
import com.example.gogoma.data.model.FilterItem
import com.example.gogoma.data.model.FilterValue
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
    var marathonTypeList by mutableStateOf<List<Int>>(emptyList())
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

    val filterContents: Map<String, List<FilterItem>>
        get() = mapOf(
            "지역" to cityList.map { FilterItem(it, FilterValue.StringValue(it)) },  // 동적으로 cityList를 반영
            "접수 상태" to listOf(
                FilterItem("접수중", FilterValue.StringValue("OPEN")),
                FilterItem("접수 마감", FilterValue.StringValue("FINISHED"))
            ),
            "종목" to marathonTypeList.map { FilterItem(formattedDistance(it), FilterValue.IntValue(it)) }, // 동적으로 marathonTypeList를 반영
            "년도" to (2025 downTo 2000).map { FilterItem("${it}년", FilterValue.StringValue("${it}")) },
            "월" to (1..12).map { FilterItem("${it}월", FilterValue.StringValue("${it}")) }
        )

    fun formattedDistance(courseType: Int): String {
        val kmValue = courseType / 100000.0
        return if (kmValue % 1 == 0.0) {
            // 소수점이 0일 때는 정수로 표시
            "${kmValue.toInt()}km"
        } else {
            // 소수점이 있을 때는 소수점 2자리까지 표시
            "%.3fkm".format(kmValue)
        }
    }

    init {
        loadMarathons()
    }

    private fun loadMarathons() {
        isLoading = true
        errorMessage = null
        viewModelScope.launch {
            try {
                val marathonStatusValue = selectedFilters.marathonStatus?.value?.let { (it as FilterValue.StringValue).value }
                val cityValue = selectedFilters.city?.value?.let { (it as FilterValue.StringValue).value }
                val yearValue = selectedFilters.year?.value?.let { (it as FilterValue.StringValue).value }
                val monthValue = selectedFilters.month?.value?.let { (it as FilterValue.StringValue).value }
                val courseTypeValues = selectedFilters.courseTypeList?.map { (it.value as? FilterValue.IntValue)?.value }
                    ?.filterNotNull()

                val response = RetrofitInstance.marathonApiService.getMarathons(
                    marathonStatusValue,
                    cityValue,
                    yearValue,
                    monthValue,
                    courseTypeValues,
                    selectedFilters.keyword
                )
                if (response.isSuccessful) {
                    marathonSearchResponseList = response.body()?.marathonPreviewDtoList ?: emptyList()
                    cityList = response.body()?.cityList ?: emptyList()
                    marathonTypeList = response.body()?.marathonTypeList ?: emptyList()
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
        marathonStatus: FilterItem? = selectedFilters.marathonStatus,
        city: FilterItem? = selectedFilters.city,
        year: FilterItem? = selectedFilters.year,
        month: FilterItem? = selectedFilters.month,
        courseTypeList: List<FilterItem>? = selectedFilters.courseTypeList,
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
        city: FilterItem? = null,
        marathonStatus: FilterItem? = null,
        year: FilterItem? = null,
        month: FilterItem? = null,
        courseTypeList: List<FilterItem>? = null
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