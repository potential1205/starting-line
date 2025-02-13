package com.example.gogoma.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gogoma.GlobalApplication

class PaceViewModelFactory(private val globalApplication: GlobalApplication) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PaceViewModel::class.java)) {
            return PaceViewModel(globalApplication) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
