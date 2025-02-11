package com.example.gogoma.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HashTag(text: String?) {
    Box(
        modifier = Modifier
            .background(color = Color(0xFFE9ECEA))
            .padding(4.dp, 2.dp)
    ){
        Text(
            text = "# "+text,
            style = TextStyle(
            fontSize = 11.9.sp)
        )
    }
}