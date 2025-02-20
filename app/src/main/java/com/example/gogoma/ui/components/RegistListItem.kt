package com.example.gogoma.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gogoma.data.dto.UserMarathonSearchDto
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun RegistListItem(regist: UserMarathonSearchDto, onClick: () -> Unit, background: Color = MaterialTheme.colorScheme.background) {
    Row (
        modifier = Modifier
            .clickable(onClick = onClick)
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        Column (
            verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.Top),
            horizontalAlignment = Alignment.Start,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // 연한 회색 날짜 (예: 24.06.18)
                fun formatDateTimeUsingLocalDateTime(input: String): String {
                    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
                    val outputFormatter = DateTimeFormatter.ofPattern("yy. M. d. HH:mm:ss")

                    val dateTime = LocalDateTime.parse(input, inputFormatter)  // 문자열을 LocalDateTime으로 변환
                    return dateTime.format(outputFormatter)  // 원하는 형식으로 변환
                }

                Text(
                    text = formatDateTimeUsingLocalDateTime(regist.paymentDateTime),
                    fontSize = 12.sp,
                    color = Color.Gray.copy(alpha = 0.5f)
                )

                // '상세보기 >' 버튼
                Text(
                    text = "상세보기 >",
                    fontSize = 12.sp,
                    color = Color.Gray.copy(alpha = 0.7f),
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
//                    .padding(3.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // 마라톤 제목
                Text(
                    text = regist.marathonTitle,
                    style = TextStyle(
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 1.dp),
                    overflow = TextOverflow.Ellipsis
                )

                // 오른쪽 'D-50' 배지
                regist.dday?.let {
                    Text(
                        text = it,
                        style = TextStyle(
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onPrimary
                        ),
                        modifier = Modifier
                            .background(
                                // 지난 마라톤이면 회색, 아니면 BrandColor
                                color = if (regist.dday.contains("D+")) Color(0xFF9C9C9C) else MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }
            }

        }

    }
}
