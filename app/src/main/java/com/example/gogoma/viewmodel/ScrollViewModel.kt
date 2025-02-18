package com.example.gogoma.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ScrollViewModel : ViewModel() {
    var scrollIndex by mutableStateOf(0)
    var scrollOffset by mutableStateOf(0)
}