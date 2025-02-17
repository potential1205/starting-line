package com.example.gogoma.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gogoma.R

@Composable
fun FilterChip(
    text : String? = null,
    onCLick : (() -> Unit)? = null,
    icon : Int? = null,
    modifier: Modifier = Modifier,
    isPoint: Boolean = false,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = Color(0xFF9C9C9C),
){
    Row(
        modifier = Modifier
            .border(width = 0.6.dp, color = Color(0xFFDADADA), shape = RoundedCornerShape(size = 16.dp))
            .height(32.2.dp)
            .background(color = backgroundColor, shape = RoundedCornerShape(size = 16.dp))
            .padding(start = 10.dp, top = 7.dp, end = 10.dp, bottom = 7.dp)
            .clickable (onClick = { onCLick?.invoke() }),
        horizontalArrangement = Arrangement.spacedBy(2.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // 아이콘이 있을 경우 표시
        icon?.let {
            Icon(
                painter = painterResource(id = it),
                contentDescription = "icon",
                tint = contentColor,
                modifier = Modifier.size(18.2.dp)
            )
        }

        // 텍스트가 있을 경우 표시
        text?.takeIf { it.isNotEmpty() }?.let {
            Text(
                text = text,
                color = contentColor,
                fontSize = 14.sp,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FilterChipPreview(){
    Column {
        FilterChip(
            text = "칩 테스트",
            backgroundColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.onTertiary,
            icon = R.drawable.logo_image,
            onCLick = { /* 클릭 시 동작 */ }
        )
        FilterChip(
            text = "칩 테스트",
            icon = R.drawable.logo_image,
            onCLick = { /* 클릭 시 동작 */ }
        )
    }
}