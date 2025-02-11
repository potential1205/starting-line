package com.example.gogoma.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.gogoma.ui.components.Regist

class RegistViewModel : ViewModel() {
    private val _registList = MutableStateFlow<List<Regist>>(emptyList())
    val registList: StateFlow<List<Regist>> = _registList

    fun addRegist(newRegist: Regist) {
        _registList.value = _registList.value + newRegist
    }
}
