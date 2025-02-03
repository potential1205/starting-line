package com.example.gogoma.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gogoma.data.model.Address
import com.example.gogoma.ui.components.Regist
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PaymentViewModel : ViewModel() {
    // 저장된 주소 리스트
    private val _addressList = MutableStateFlow(loadSavedAddresses())
    val addressList: StateFlow<List<Address>> = _addressList

    // 현재 선택된 주소 (기본값: 기본 배송지)
    private val _selectedAddress = MutableStateFlow(_addressList.value.firstOrNull { it.isDefault })
    val selectedAddress: StateFlow<Address?> = _selectedAddress

    // 현재 선택된 사이즈
    private val _selectedSize = MutableStateFlow("95")
    val selectedSize: StateFlow<String> = _selectedSize

    // 참가 종목 선택 상태
    private val _selectedDistance = MutableStateFlow("5km")
    val selectedDistance: StateFlow<String> = _selectedDistance

    // 결제 수단 선택 상태
    private val _selectedPayment = MutableStateFlow("카카오페이")
    val selectedPayment: StateFlow<String> = _selectedPayment

    // 약관 동의 상태
    private val _isAgreementChecked = MutableStateFlow(false)
    val isAgreementChecked: StateFlow<Boolean> = _isAgreementChecked

    private val _registInfo = MutableStateFlow<Regist?>(null)
    val registInfo: StateFlow<Regist?> = _registInfo.asStateFlow()

    // 배송지 선택 업데이트
    fun selectAddress(address: Address) {
        viewModelScope.launch {
            _selectedAddress.value = address
        }
    }

    // 사이즈 선택 업데이트
    fun updateSelectedSize(newSize: String) {
        viewModelScope.launch {
            _selectedSize.value = newSize
        }
    }

    // 참가 종목 선택 업데이트
    fun updateSelectedDistance(newDistance: String) {
        viewModelScope.launch {
            _selectedDistance.value = newDistance
        }
    }

    // 결제 수단 선택 업데이트
    fun updateSelectedPayment(newPayment: String) {
        viewModelScope.launch {
            _selectedPayment.value = newPayment
        }
    }

    // 약관 동의 상태 업데이트
    fun updateAgreementChecked(isChecked: Boolean) {
        viewModelScope.launch {
            _isAgreementChecked.value = isChecked
        }
    }

    fun saveRegistInfo(regist: Regist) {
        _registInfo.value = regist
    }

    class PaymentViewModel : ViewModel() {
        // 기존 상태 변수들 유지...

        var isBottomSheetVisible by mutableStateOf(false)
            private set // private으로 설정

        fun showBottomSheet(){
            isBottomSheetVisible = true
        }

        fun hideBottomSheet(){
            isBottomSheetVisible = false
        }
    }




    // 기본 주소 및 데이터 불러오기 함수
    companion object {
        fun loadSavedAddresses(): List<Address> {
            return listOf(
                Address("1", "홍길동", "서울특별시 영등포구 선유로 00 현대아파트", "101동 202호", "010-0000-0000", isDefault = true),
                Address("2", "김이름", "서울특별시 강남구 테헤란로 00", "302동 502호", "010-1234-5678"),
                Address("3", "박철수", "부산광역시 해운대구 달맞이길 00", "100동 1001호", "010-9876-5432")
            )
        }
    }
}
