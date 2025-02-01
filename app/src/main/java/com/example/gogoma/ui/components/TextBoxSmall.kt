package com.example.gogoma.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TextBoxSmall(
    text: String,
    textColor: Color = MaterialTheme.colorScheme.onSecondary,
    backgroundColor: Color = MaterialTheme.colorScheme.secondary,
){
    Box(
        modifier = Modifier
            .background(color = backgroundColor, shape = RoundedCornerShape(5.dp))
            .padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text= text, style = TextStyle(color = textColor, fontSize = 13.sp))
    }
}