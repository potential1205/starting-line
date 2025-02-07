package com.ssafy.gogomawatch.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.Text
import com.ssafy.gogomawatch.R
import com.ssafy.gogomawatch.presentation.components.ProgressBar

@Composable
fun TeamScreen() {
    Box() {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                progress = 0.5f,
                modifier = Modifier.fillMaxSize(),
                strokeWidth = 10.dp,
                indicatorColor = Color.Green,
                trackColor = Color.Green.copy(alpha = 0.2f)
            )
        }
        Row (
            Modifier.fillMaxSize()
                .padding(30.dp)
        ) {
            Box(
                Modifier.fillMaxWidth(0.4f)
                    .fillMaxHeight()
                    .padding(start = 5.dp, end = 5.dp)
            ) {
                Box(
                    Modifier.fillMaxSize()
                        .background(color = Color.White)
                )
                Box(
                    Modifier.fillMaxSize()
                        .align(Alignment.Center)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_image), // 로고 이미지 리소스
                        contentDescription = "Logo",
                        modifier = Modifier
                            .size(10.dp) // 크기를 100dp로 설정
                            .align(Alignment.Center) // 가운데 정렬
                    )
                }
            }
            // 예시 데이터 리스트
            val itemsList = listOf(
                "1tem",
                "2tem",
                "3tem",
                "4tem",
                "5tem",
                "6tem",
                "7tem",
                "8tem",
                "9tem",
                "0tem"
            )

            LazyColumn (
                Modifier
                    .fillMaxHeight()
            ){
                items(itemsList) { item ->
                    Box(
                        Modifier.height(50.dp)
                    ) {
                        Text(text = item,
                            fontSize = 30.sp)
                    } // 리스트 아이템을 텍스트로 표시
                }
            }
        }
    }

}

@Preview
@Composable
fun TeamScreenPreview() {
    TeamScreen()
}