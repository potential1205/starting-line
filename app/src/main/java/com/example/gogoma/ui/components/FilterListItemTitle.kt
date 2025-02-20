package com.example.gogoma.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FilterListItemTitle(title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .then(
                Modifier.drawBehind {
                    val strokeWidth = 1.dp.toPx()
                    val y = size.height - strokeWidth / 2
                    drawLine(
                        color = Color(0xFFE7E7E7), // 하단 선의 색상
                        start = androidx.compose.ui.geometry.Offset(0f, y),
                        end = androidx.compose.ui.geometry.Offset(size.width, y),
                        strokeWidth = strokeWidth
                    )
                }
            )
            .padding(start = 5.dp, top = 18.dp, end = 5.dp, bottom = 18.dp),
        horizontalArrangement = Arrangement.spacedBy(0.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            style = TextStyle(
                fontSize = 14.5.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF000000),
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FilterListItemTitlePreview(){
    FilterListItemTitle("지역")
}