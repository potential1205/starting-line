package com.example.gogoma.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.gogoma.R

@Composable
fun Filter(onFilterClick : (String) -> Unit){
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ){
        Row(
            modifier = Modifier
                .horizontalScroll(scrollState)
                .fillMaxWidth()
                .height(62.2.dp)
                .padding(start = 18.dp, top = 13.dp, end = 18.dp, bottom = 17.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            FilterChip(
                backgroundColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.onTertiary,
                icon = R.drawable.icon_tune,
                onCLick = {
                    onFilterClick("기본 필터")
                },
            )
            FilterChip(
                text = "지역",
                onCLick = {
                    onFilterClick("지역 필터")
                }
            )
            FilterChip(
                text = "접수 상태",
                onCLick = {
                    onFilterClick("접수 상태 필터")
                }
            )
            FilterChip(
                text = "종목",
                onCLick = {
                    onFilterClick("종목 필터")
                }
            )
            FilterChip(
                text = "년도",
                onCLick = {
                    onFilterClick("년도 필터")
                }
            )
            FilterChip(
                text = "월",
                onCLick = {
                    onFilterClick("월 필터")
                }
            )
        }

        // 하단 border
        Canvas(modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
        ) {
            val strokeWidth = 1.dp.toPx()
            val y = size.height - strokeWidth / 2
            drawLine(
                color = Color(0xFFE7E7E7),
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = strokeWidth
            )
        }
    }

}