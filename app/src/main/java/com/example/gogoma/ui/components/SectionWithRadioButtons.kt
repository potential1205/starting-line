package com.example.gogoma.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gogoma.R
import com.example.gogoma.theme.GogomaTheme

@Composable
fun SectionWithRadioButtons(
    ifFormatNeed: Boolean = false,
    title: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .border(width = 0.4.dp, color = Color(0xFFE4E4E4), shape = RoundedCornerShape(size = 16.dp))
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background, shape = RoundedCornerShape(size = 16.dp))
            .padding(start = 20.dp, top = 25.dp, end = 20.dp, bottom = 25.dp),
    ) {
        // 제목
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
        )

        // 라디오 버튼 리스트
        Column(
            modifier = Modifier.padding(top = 15.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
        ) {
            options.forEach { option ->
                val formattedOption = formatOption(option)

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOptionSelected(option) }
                ) {
                    // 선택 여부에 따른 아이콘 변경
                    IconButton(
                        onClick = { onOptionSelected(option) },
                        modifier = Modifier.size(30.dp)
                    ) {
                        Icon(
                            painter = painterResource(
                                id = if (option == selectedOption) R.drawable.radio_button_checked
                                else R.drawable.radio_button_default
                            ),
                            contentDescription = "Radio button for $option",
                            tint = if (option == selectedOption) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.size(30.dp)
                        )
                    }

                    Text(
                        text = if(ifFormatNeed) formattedOption else option,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}

private fun formatOption(option: String): String {
    val parts = option.split(" - ")
    val distanceString = parts.firstOrNull() ?: "0"
    val priceString = parts.getOrNull(1) ?: ""

    val cmValue = distanceString.filter { it.isDigit() }.toIntOrNull() ?: 0
    val kmValue = cmValue / 100000.0

    val formattedDistance = if (kmValue % 1 == 0.0) {
        "${kmValue.toInt()} km"
    } else {
        "%.3f km".format(kmValue)
    }

    return "$formattedDistance - $priceString"
}

@Preview(showBackground = true)
@Composable
fun SectionWithRadioButtonsPreview() {
    var selected by remember { mutableStateOf("5km") }

    GogomaTheme {
        SectionWithRadioButtons(
            title = "참가 종목",
            options = listOf("500000 - 15000원", "1000000 - 29000원"),
            selectedOption = selected,
            onOptionSelected = { selected = it }
        )
    }
}
