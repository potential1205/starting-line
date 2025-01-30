package com.example.gogoma.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gogoma.R
import com.example.gogoma.theme.GogomaTheme

@Composable
fun SectionWithRadioButtons(
    title: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        // 제목
        Text(
            text = title,
            fontSize = 18.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // 구분선
        HorizontalDivider(
            modifier = Modifier.padding(bottom = 8.dp),
            thickness = 1.dp,
            color = Color.Gray
        )

        // 라디오 버튼 리스트
        Column {
            options.forEach { option ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .background(MaterialTheme.colorScheme.background, RoundedCornerShape(8.dp))
                ) {
                    // 선택 여부에 따른 아이콘 변경
                    IconButton(
                        onClick = { onOptionSelected(option) },
                        modifier = Modifier.size(32.dp) // 기존보다 약간 키움
                    ) {
                        Icon(
                            painter = painterResource(
                                id = if (option == selectedOption) R.drawable.radio_button_checked
                                else R.drawable.radio_button_default
                            ),
                            contentDescription = "Radio button for $option",
                            tint = Color.Unspecified // 원본 색상 유지
                        )
                    }

                    Text(
                        text = option,
                        fontSize = 16.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SectionWithRadioButtonsPreview() {
    var selected by remember { mutableStateOf("5km") }

    GogomaTheme {
        SectionWithRadioButtons(
            title = "참가 종목",
            options = listOf("5km", "10km", "하프", "풀"),
            selectedOption = selected,
            onOptionSelected = { selected = it }
        )
    }
}
