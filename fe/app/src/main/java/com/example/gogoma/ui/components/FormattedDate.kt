package com.example.gogoma.ui.components

import androidx.compose.runtime.Composable
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun FormattedDate(dateString : String) : String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.KOREA)
    val outputFormat = SimpleDateFormat("yyyy년 M월 d일 a h시", Locale.KOREA)

    return try {
        val date = inputFormat.parse(dateString)
        outputFormat.format(date)
    } catch (e: Exception) {
        "Invalid Date"
    }
}