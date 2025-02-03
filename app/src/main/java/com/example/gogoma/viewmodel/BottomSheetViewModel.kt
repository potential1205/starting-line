package com.example.gogoma.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class BottomSheetViewModel : ViewModel() {
    // 필터 보이기 상태
    var isBottomSheetVisible by mutableStateOf(false)
        private set // private으로 설정

    // 모달 내 화면 전환 상태 (하위 페이지가 열려 있는지 여부)
    var isSubPageVisible by mutableStateOf(false)
        private set

    // 현재 모달의 타입 (필터, 다른 모달 등)
    var modalType by mutableStateOf("기본")
        private set

    // 현재 모달 내 페이지 상태
    var pageName by mutableStateOf("기본")
        private set

    fun showBottomSheet(page: String = "기본"){
        isBottomSheetVisible = true
        isSubPageVisible = false  // 처음 모달을 열 때는 첫 번째 화면을 보여줌
        pageName = page  // 모달을 열면 주어진 페이지로 설정
    }

    fun hideBottomSheet(){
        isBottomSheetVisible = false
        isSubPageVisible = false  // 모달을 닫을 때 하위 페이지 상태도 초기화
        pageName = "기본"  // 모달 닫을 때 페이지도 기본 상태로 초기화
    }

    // 하위 페이지로 전환 (모달 내에서 화면 변경)
    fun showSubPage(page: String, modalType: String = "기본") {
        this.pageName = page
        this.modalType = modalType
        isSubPageVisible = true
    }

    // 하위 페이지에서 이전 화면으로 돌아가기
    fun goBackToPreviousPage() {
        isSubPageVisible = false
        pageName = "기본"  // 기본 화면으로 돌아가면 필터를 초기화
        modalType = "기본"  // 모달 타입도 기본으로 초기화
    }
}