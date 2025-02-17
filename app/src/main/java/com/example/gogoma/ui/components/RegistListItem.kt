package com.example.gogoma.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gogoma.R
import com.example.gogoma.data.dto.UserMarathonSearchDto

@Composable
fun RegistListItem(regist: UserMarathonSearchDto, onClick: () -> Unit) {
    Row (
        modifier = Modifier
            .clickable(onClick = onClick)
            .width(412.dp)
            .height(103.dp)
            .background(MaterialTheme.colorScheme.background)
            .padding(start = 19.dp, top = 13.dp, end = 19.dp, bottom = 13.dp)
    ) {
        Column (
            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.Top),
            horizontalAlignment = Alignment.Start,
        ) {
            Row(
                modifier = Modifier
                    .width(374.dp)
                    .height(17.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // 연한 회색 날짜 (예: 24.06.18)
                Text(
                    text = regist.paymentDateTime,
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
                    .width(374.dp)
                    .height(47.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column (modifier = Modifier
                    .width(214.dp)
                    .wrapContentHeight()
                ) {
                    // 마라톤 제목
                    Text(
                        text = regist.marathonTitle,
                        style = TextStyle(
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        ),
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // 마라톤 날짜 및 거리 정보
                    Row(modifier = Modifier
                        .width(214.dp)
                        .height(16.dp)) {
                        Text(
                            text = regist.raceStartDateTime,
                            style = TextStyle(
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        )

                        Spacer(Modifier.width(10.dp))

                        Text(
                            text = regist.marathonType,
                            style = TextStyle(
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        )
                    }

                }
                // 오른쪽 'D-50' 배지
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    regist.dday?.let {
                        Text(
                            text = it,
                            style = TextStyle(
                                fontSize = 12.sp,
                                color = Color.White
                            ),
                            modifier = Modifier
                                .background(
                                    // 지난 마라톤이면 회색, 아니면 초록색
                                    color = if (regist.dday.equals("D-?")) Color.Gray.copy(alpha = 0.5f) else Color(0xFF4CAF50),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                }
            }

        }

    }
}
