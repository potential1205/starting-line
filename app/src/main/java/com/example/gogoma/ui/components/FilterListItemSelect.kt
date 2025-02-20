package com.example.gogoma.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gogoma.R

@Composable
fun FilterListItemSelect(
    title: String,
    content: String,
    onClick: () -> Unit
){
    Row (
        modifier = Modifier
            .clickable { onClick() }
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
            .padding(start = 15.dp, top = 18.dp, end = 9.dp, bottom = 18.dp),
        horizontalArrangement = Arrangement.spacedBy(0.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column (
            modifier = Modifier
                .weight(1f)
                .padding(top = 5.dp, bottom = 5.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                text = title,
                style = TextStyle(
                    fontSize = 14.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF000000),
                )
            )
            Text(
                text = content,
                style = TextStyle(
                    fontSize = 14.5.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFFB3B3B3),
                )
            )
        }
        IconButton(
            onClick = {  },
            modifier = Modifier.size(17.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_arrow_forward_ios),
                contentDescription = "forward arrow icon",
                tint = MaterialTheme.colorScheme.onBackground,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FilterListItemSelectPreview(){
    FilterListItemSelect("지역", "서울특별시", onClick={})
}