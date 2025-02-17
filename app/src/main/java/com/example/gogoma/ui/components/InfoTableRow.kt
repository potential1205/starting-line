package com.example.gogoma.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gogoma.R
import com.example.gogoma.theme.GogomaTheme

@Composable
fun InfoTableRow(label: String, value: String) {
    Row (
        modifier =  Modifier
            .width(387.dp)
            .wrapContentHeight() // 내부 콘텐츠 크기에 맞게 높이 조정
            .padding(top = 2.dp, bottom = 2.dp),
        horizontalArrangement = Arrangement.spacedBy(50.dp, Alignment.Start),
        verticalAlignment = Alignment.Top,
    ) {
        Text(
            text = label,
            style = TextStyle(
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onBackground,
            )
        )

        Text(
            text = value,
            style = TextStyle(
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onBackground,
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun InfoTableRowPreview() {
    GogomaTheme {
        InfoTableRow(label = "대회 가격", value = "풀코스 : 50,000원 (마니아 : 35,000원)\n하프코스 : 45,000원 (마니아 : 35,000원)\n10km코스 : 40,000원 (마니아 : 35,000원)\n5km : 30,000원")
    }
}