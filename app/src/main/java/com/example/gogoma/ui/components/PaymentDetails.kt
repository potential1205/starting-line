package com.example.gogoma.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
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
import com.example.gogoma.data.model.PaymentDetail
import com.example.gogoma.theme.GogomaTheme

@Composable
fun PaymentDetails (paymentDetail: PaymentDetail) {
    Column (
        modifier = Modifier
            .width(410.dp)
            .height(247.dp)
            .background(color = Color(0xFFFFFFFF))
            .padding(start = 15.dp, top = 12.dp, end = 15.dp, bottom = 12.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.Top),
        horizontalAlignment = Alignment.Start,
    ) {
        Row (
            modifier = Modifier
                .width(380.dp)
                .height(62.dp)
                .padding(start = 10.dp, top = 25.dp, end = 10.dp, bottom = 15.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "결제 내역",
                style = TextStyle(
                    fontSize = 19.sp,
                    fontFamily = FontFamily(Font(R.font.nanum_square_round_b)),
                    color = Color(0xFF000000),
                )
            )
        }
        Column (
            modifier = Modifier
                .width(380.dp)
                .height(12.dp)
                .padding(start = 3.dp, top = 2.dp, end = 3.dp, bottom = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            HorizontalDivider(
                modifier = Modifier
                    .padding(0.dp)
                    .width(374.dp)
                    .height(1.dp),
                color = Color(0xFFDDDDDD)
            )
        }
        Column(
            modifier = Modifier
                .width(380.dp)
                .height(149.dp)
                .padding(start = 5.dp, end = 10.dp),
            verticalArrangement = Arrangement.spacedBy(7.dp, Alignment.Top),
            horizontalAlignment = Alignment.Start,
        ) {
            // 결제 내역 정보
            InfoTableRow(label = "결제날짜", value = paymentDetail.paymentDate)
            InfoTableRow(label = "결제수단", value = paymentDetail.paymentType)
            InfoTableRow(label = "결제금액", value = paymentDetail.paymentAmount)
            InfoTableRow(label = "배송주소", value = paymentDetail.address)
            InfoTableRow(label = "신청종목", value = paymentDetail.raceCategory)
//            InfoTableRow(label = "기념품목", value = paymentDetail.gift ?: "정보없음")
        }
    }
}

@Preview
@Composable
fun PaymentDatailsPreview() {
    GogomaTheme {
        PaymentDetails(paymentDetail = dummyPaymentDetails)
    }
}

// 더미데이터
val dummyPaymentDetails = PaymentDetail(
    paymentDate = "2025-01-29",
    paymentType = "신용카드",
    paymentAmount = "50,000원",
    address = "서울특별시 강남구 테헤란로 123",
    raceCategory = "5km",
    gift = "반팔(95)"
)