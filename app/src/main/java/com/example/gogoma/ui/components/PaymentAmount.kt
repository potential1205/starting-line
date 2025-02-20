package com.example.gogoma.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gogoma.theme.BrandColor1
import com.example.gogoma.theme.GogomaTheme
import com.example.gogoma.theme.NeutralLight

@Composable
fun PaymentAmount(
    amount: Int
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 0.4.dp, color = NeutralLight, shape = RoundedCornerShape(size = 16.dp))
            .background(color = MaterialTheme.colorScheme.background, shape = RoundedCornerShape(size = 16.dp))
    ) {
        // 결제 금액 텍스트
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 25.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "결제 금액",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Row {
                Text(
                    text = "%,d원".format(amount), // 쉼표 추가된 금액 표시
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
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
