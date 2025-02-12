package com.example.gogoma.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gogoma.data.dto.UserMarathonSearchDto
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// 뛴 마라톤, 뛸 마라톤
@Composable
fun RegistMarathonCountSection(registList: List<UserMarathonSearchDto>) {
    // 지난 마라톤과 앞으로 뛸 마라톤 개수 계산
    val (pastCount, upcomingCount) = if (registList.isEmpty()) {
        // 리스트가 비었을 때 0으로 설정
        0 to 0
    } else {
        registList.partition { regist ->
            regist.dDay?.equals("D-?") == true
        }.let { (past, upcoming) -> past.size to upcoming.size }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "뛸 마라톤", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text(text = "$upcomingCount", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4CAF50))
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "뛴 마라톤", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text(text = "$pastCount", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }
}