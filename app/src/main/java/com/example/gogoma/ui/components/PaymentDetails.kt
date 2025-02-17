package com.example.gogoma.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gogoma.data.dto.UserMarathonDetailDto

@Composable
fun PaymentDetails (paymentDetail: UserMarathonDetailDto) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, top = 10.dp, end = 20.dp, bottom = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.Top),
        horizontalAlignment = Alignment.Start,
    ) {
        // 결제 내역 정보
        InfoTableRow(label = "결제날짜", value = paymentDetail.paymentDateTime)
        InfoTableRow(label = "결제수단", value = paymentDetail.paymentType.toString())
        InfoTableRow(label = "결제금액", value = paymentDetail.paymentAmount)
        InfoTableRow(label = "배송주소", value = paymentDetail.address)
        InfoTableRow(label = "신청종목", value = paymentDetail.selectedCourseType)
//            InfoTableRow(label = "기념품목", value = paymentDetail.gift ?: "정보없음")
    }
}