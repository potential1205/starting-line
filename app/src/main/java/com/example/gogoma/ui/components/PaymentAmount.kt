package com.example.gogoma.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gogoma.theme.BrandColor1
import com.example.gogoma.theme.GogomaTheme

@Composable
fun PaymentAmount(
    amount: Int
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp) // 기존과 동일한 패딩 적용
    ) {
        // 결제 금액 텍스트
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "결제 금액",
                fontSize = 18.sp,
                color = Color.Black
            )

            Row {
                Text(
                    text = "%,d".format(amount), // 쉼표 추가된 금액 표시
                    fontSize = 18.sp,
                    color = Color.Black
                )
                Text(
                    text = "원", // "원"만 초록색
                    fontSize = 18.sp,
                    color = BrandColor1 // 초록색 적용
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PaymentAmountPreview() {
    GogomaTheme {
        PaymentAmount(amount = 50000)
    }
}
