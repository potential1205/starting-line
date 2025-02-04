package com.example.gogoma.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.gogoma.R

@Composable
fun ButtonKakao(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    val aspectRatio = 600f / 90f //카카오 버튼 원본 이미지 크기
    Image(
        painter = painterResource(id = R.drawable.kakaobutton), // 카카오 버튼 이미지
        contentDescription = "Button of Kakao",
        modifier = modifier
            .aspectRatio(aspectRatio)
            .clickable {
                onClick()
            }
    )
}

@Preview(showBackground = true)
@Composable
fun ButtonKakaoPreview() {
    ButtonKakao(modifier = Modifier.fillMaxWidth())
}