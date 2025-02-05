package com.example.gogoma.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gogoma.R

@Composable
fun ButtonBasic (
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    iconResId: Int? = null, // 아이콘 리소스 (nullable)
    iconSize: Dp = 24.dp, // 아이콘 사이즈 (기본값 24.dp)
    backgroundColor: Color = MaterialTheme.colorScheme.primary, // 기본 색상
    contentColor: Color = MaterialTheme.colorScheme.onPrimary, // 텍스트 색상
    round: Dp = 8.dp, // 모서리 둥글기 정도 (기본값 8.dp)
    contentPaddingTop: Dp = 4.dp, // 상단 padding
    contentPaddingBottom: Dp = 4.dp, // 하단 padding
    contentPaddingStart: Dp = 8.dp, // 좌측 padding
    contentPaddingEnd: Dp = 8.dp // 우측 padding
) {
    Button (
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(round), // 모서리 둥글게
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = contentColor
        ),
        contentPadding = PaddingValues(
            top = contentPaddingTop,
            bottom = contentPaddingBottom,
            start = contentPaddingStart,
            end = contentPaddingEnd
        )
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            iconResId?.let {
                Icon(
                    painter = painterResource(id = it),
                    contentDescription = "Button Icon",
                    modifier = Modifier.padding(end = 8.dp).size(iconSize)
                )
            }
            Text(text = text, fontSize = 16.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ButtonBasicPreview() {
    ButtonBasic(
        text = "로그인",
        onClick = { /* 로그인 버튼 클릭 시 동작 */ }
    )
}

@Preview(showBackground = true)
@Composable
fun ButtonBasicPreviewWithIcon() {
    ButtonBasic(
        text = "로그인",
        iconResId = R.drawable.logo_image, // 로그인 아이콘 리소스
        onClick = { /* 로그인 버튼 클릭 시 동작 */ }
    )
}