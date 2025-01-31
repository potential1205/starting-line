package com.example.gogoma.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class BottomSheetViewModel : ViewModel() {
    // 필터 보이기 상태
    var isBottomSheetVisible by mutableStateOf(false)
        private set // private으로 설정

    var selectedFilter by mutableStateOf("필터 선택")
        private set

    fun showBottomSheet(){
        isBottomSheetVisible = true
    }

    fun hideBottomSheet(){
        isBottomSheetVisible = false
    }

    fun selectFilter(filter: String) {
        selectedFilter = filter
    }
}