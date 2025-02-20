package com.example.gogoma.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.ColorMatrixColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gogoma.R
import com.example.gogoma.theme.BrandColor1
import com.example.gogoma.theme.GogomaTheme

@Composable
fun BottomBarButton(
    isKakaoPay: Boolean = false,
    text: String,
    backgroundColor: Color,
    textColor: Color,
    onClick: () -> Unit,
    enabled: Boolean = true // 기본값 true, false일 경우 버튼 비활성화
) {
    HorizontalDivider(
        modifier = Modifier
            .fillMaxWidth(),
        thickness = 1.dp,
        color = Color(0xFFE5E5E5)
    )
    Button(
        onClick = onClick,
        enabled = enabled, // 비활성화 적용
        colors = ButtonDefaults.buttonColors(
            containerColor = if (enabled) backgroundColor else Color.Gray, // 비활성화 시 회색 처리
            disabledContainerColor = (if(isKakaoPay) Color(0xFFDEDEDE) else Color.Gray) // 비활성화 시 배경색
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 7.5.dp)
            .height(50.dp) // 버튼 높이 설정
    ) {
        Box (
            modifier = Modifier.fillMaxWidth()
        ) {
            if(isKakaoPay){
                val aspectRatio = 242f / 100f //카카오페이 원본 이미지 크기
                val colorFilter = if (!enabled) {
                    ColorMatrixColorFilter(ColorMatrix().apply {
                        setToSaturation(0f) // 채도 0으로 설정
                    })
                } else {
                    null
                }
                Image (
                    painter = painterResource(id = R.drawable.payment_icon_yellow_large), // 카카오 버튼 이미지
                    contentDescription = "Button of KakaoPay",
                    colorFilter = colorFilter,
                    modifier = Modifier
                        .padding(top = 4.dp, end = 160.dp)
                        .height(25.dp)
                        .aspectRatio(aspectRatio)
                        .align(Alignment.Center)
                )
            }
            Text(
                text = text,
                fontSize = 15.sp,
                fontWeight = FontWeight.ExtraBold,
                color = if (enabled) textColor else (if(isKakaoPay) Color.Black else Color.LightGray), // 비활성화 시 텍스트 색 연하게
                modifier = Modifier.align(Alignment.Center).padding(start = if(isKakaoPay) 60.dp else 0.dp)
            )
        }


    }

}

@Preview(showBackground = true)
@Composable
fun BottomBarButtonPreview() {
    GogomaTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 0.dp)
        ) {
            // 활성화된 버튼 (기본)
            BottomBarButton(
                text = "40,000원 결제하기",
                backgroundColor = BrandColor1,
                textColor = Color.White,
                onClick = {}
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 활성화된 버튼 (카카오페이)
            BottomBarButton(
                isKakaoPay = true,
                text = "40,000원 결제하기",
                backgroundColor = Color(0xFFFFEB00),
                textColor = Color(0xFF000000),
                onClick = {}
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 비활성화된 버튼
            BottomBarButton(
                text = "0원 결제하기",
                backgroundColor = Color(0xFF62DA74),
                textColor = Color.White,
                onClick = {},
                enabled = false // 비활성화
            )

            // 비활성화된 버튼 (카카오페이)
            BottomBarButton(
                isKakaoPay = true,
                text = "0원 결제하기",
                backgroundColor = Color(0xFFDEDEDE),
                textColor = Color.White,
                onClick = {},
                enabled = false
            )
        }
    }
}
