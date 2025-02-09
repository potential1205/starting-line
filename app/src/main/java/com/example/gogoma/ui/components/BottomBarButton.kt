package com.example.gogoma.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gogoma.R
import com.example.gogoma.theme.BrandColor1
import com.example.gogoma.theme.GogomaTheme
import com.example.gogoma.theme.NanumSquareRound

@Composable
fun BottomBarButton(
    text: String,
    backgroundColor: Color,
    textColor: Color,
    onClick: () -> Unit,
    enabled: Boolean = true // 기본값 true, false일 경우 버튼 비활성화
) {
    Button(
        onClick = onClick,
        enabled = enabled, // 비활성화 적용
        colors = ButtonDefaults.buttonColors(
            containerColor = if (enabled) backgroundColor else Color.Gray, // 비활성화 시 회색 처리
            disabledContainerColor = Color.Gray // 비활성화 시 배경색
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .height(50.dp) // 버튼 높이 설정
    ) {
        Text(
            text = text,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(Font(R.font.nanum_square_round_b)),
            color = if (enabled) textColor else Color.LightGray // 비활성화 시 텍스트 색 연하게
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BottomBarButtonPreview() {
    GogomaTheme {
        Column {
            // 활성화된 버튼 (기본)
            BottomBarButton(
                text = "결제하기",
                backgroundColor = BrandColor1,
                textColor = Color.White,
                onClick = {}
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 비활성화된 버튼
            BottomBarButton(
                text = "결제하기",
                backgroundColor = Color(0xFF62DA74),
                textColor = Color.White,
                onClick = {},
                enabled = false // 비활성화
            )
        }
    }
}
