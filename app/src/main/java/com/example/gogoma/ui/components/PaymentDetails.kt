package com.example.gogoma.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gogoma.data.dto.UserMarathonDetailDto
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun PaymentDetails (paymentDetail: UserMarathonDetailDto) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.Top),
        horizontalAlignment = Alignment.Start,
    ) {
        fun formattedDistance(courseType: Int): String {
            val kmValue = courseType / 100000.0
            return if (kmValue % 1 == 0.0) {
                // 소수점이 0일 때는 정수로 표시
                "${kmValue.toInt()} km"
            } else {
                // 소수점이 있을 때는 소수점 2자리까지 표시
                "%.3f km".format(kmValue)
            }
        }

        fun formatDateTimeUsingLocalDateTime(input: String): String {
            val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
            val outputFormatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일 HH:mm:ss")

            val dateTime = LocalDateTime.parse(input, inputFormatter)  // 문자열을 LocalDateTime으로 변환
            return dateTime.format(outputFormatter)  // 원하는 형식으로 변환
        }

        // 결제 내역 정보
        InfoTableRow(label = "결제날짜", value = formatDateTimeUsingLocalDateTime(paymentDetail.paymentDateTime))
        InfoTableRow(label = "결제수단", value = paymentDetail.paymentType)
        InfoTableRow(label = "결제금액", value = paymentDetail.paymentAmount)
        InfoTableRow(label = "배송주소", value = paymentDetail.address)
        InfoTableRow(label = "신청종목", value = formattedDistance(paymentDetail.selectedCourseType))
//            InfoTableRow(label = "기념품목", value = paymentDetail.gift ?: "정보없음")
    }
}