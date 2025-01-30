package com.example.gogoma.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gogoma.R
import com.example.gogoma.theme.GogomaTheme

// 결제 내역 데이터 클래스
data class PaymentDetail(
    val paymentDate: String,
    val paymentMethod: String,
    val paymentAmount: String,
    val address: String,
    val raceCategory: String,
    val gift: String
)

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
            PaymentRow(label = "결제날짜", value = paymentDetail.paymentDate)
            PaymentRow(label = "결제수단", value = paymentDetail.paymentMethod)
            PaymentRow(label = "결제금액", value = paymentDetail.paymentAmount)
            PaymentRow(label = "주소", value = paymentDetail.address)
            PaymentRow(label = "신청종목", value = paymentDetail.raceCategory)
            PaymentRow(label = "사은품", value = paymentDetail.gift)
        }
    }
}

// 결제 정보 표시용 Row
@Composable
fun PaymentRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier.width(50.dp) // 4글자 크기 고정
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween // 글자 간격 자동 배치
            ) {
                label.forEach { char ->
                    Text(
                        text = char.toString(),
                        style = TextStyle(
                            fontSize = 13.sp,
                            fontFamily = FontFamily(Font(R.font.nanum_square_round_r)),
                            color = Color.Black
                        )
                    )
                }
            }
        }
        Text(
            text = value,
            style = TextStyle(
                fontSize = 13.sp,
                fontFamily = FontFamily(Font(R.font.nanum_square_round_l)),
                color = Color.Black
            )
        )
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
    paymentMethod = "신용카드",
    paymentAmount = "50,000원",
    address = "서울특별시 강남구 테헤란로 123",
    raceCategory = "5km",
    gift = "반팔(M)"
)