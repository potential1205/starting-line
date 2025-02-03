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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gogoma.theme.GogomaTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// 데이터 클래스
data class Regist (
    val registrationDate: String, // 등록일 (예: "24.06.18")
    val title: String, // 마라톤 제목 (예: "2025 서울마라톤")
    val date: String, // 마라톤 날짜 (예: "2025.03.16")
    val distance: String, // 거리 (예: "10km")
//    val statusText: String // 배지 텍스트 (예: "D-50")
)

@Composable
fun RegistListItem(regist: Regist, onClick: () -> Unit) {
    var statusText by remember { mutableStateOf("") }

    // 오늘 날짜와 마라톤 날짜 비교해서 D-day 갱신
    LaunchedEffect (Unit) {
        val today = Calendar.getInstance()
        val targetDate = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA).parse(regist.date)
        val diffInMillis = targetDate.time - today.timeInMillis
        val daysLeft = (diffInMillis / (1000 * 60 * 60 * 24)).toInt()

        statusText = if (daysLeft >= 0) {
            "D-$daysLeft"
        } else {
            "지난 마라톤"
        }
    }

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
                    text = regist.registrationDate,
                    fontSize = 12.sp,
                    color = Color.Gray.copy(alpha = 0.5f)
                )

                // '상세보기 >' 버튼
                Text(
                    text = "상세보기 >",
                    fontSize = 12.sp,
                    color = Color.Gray.copy(alpha = 0.7f),
//                    modifier = Modifier.clickable { /* 상세보기 기능 추가 */ }
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
                        text = regist.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // 마라톤 날짜 및 거리 정보
                    Row(modifier = Modifier
                        .width(214.dp)
                        .height(16.dp)) {
                        Text(
                            text = "${regist.date}",
                            style = TextStyle(
                                fontSize = 14.sp,
//                                fontFamily = FontFamily(Font(R.font.nanum_square_round)),
                                fontWeight = FontWeight(300),
                                color = Color(0xFF000000),
                            )
                        )

                        Spacer(Modifier.width(10.dp))

                        Text(
                            text = "# ${regist.distance}",
                            style = TextStyle(
                                fontSize = 14.sp,
//                                fontFamily = FontFamily(Font(R.font.nanum_square_round)),
                                fontWeight = FontWeight(700),
                                color = Color(0xFF606060),
                            )
                        )
                    }

                }
                // 오른쪽 'D-50' 배지
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = statusText,
                        color = Color.White,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .background(
                                // 지난 마라톤이면 회색, 아니면 초록색
                                color = if (statusText == "지난 마라톤") Color.Gray.copy(alpha = 0.5f) else Color(0xFF4CAF50),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    )

                }
            }

        }

    }
}

@Preview
@Composable
fun RegistPreview() {
    GogomaTheme {
        RegistListItem(sampleRegist, onClick = {})
    }
}

// 샘플 데이터
val sampleRegist = Regist(
    registrationDate = "24.06.18",
    title = "2025 서울마라톤",
    date = "2025.03.16",
    distance = "10km"
)
