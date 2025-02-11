package com.example.gogoma.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gogoma.R
import com.example.gogoma.theme.GogomaTheme

@Composable
fun TopBarArrow(title: String, onBackClick: () -> Unit, refreshAction: (()->Unit)? = null) {
    val topInset = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp)
            .background(MaterialTheme.colorScheme.background)
            .padding(start = 5.dp, end = 5.dp, top = topInset),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 뒤로가기 버튼
        IconButton(
            onClick = onBackClick, // 네비게이션 핸들링 가능
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.arrow_back),
                contentDescription = "Back Arrow",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(30.dp)
            )
        }

        // 중앙 타이틀
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        if (refreshAction != null) {
            IconButton(
                onClick = refreshAction,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.icon_refresh),
                    contentDescription = "Refresh",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(30.dp)
                )
            }
        }
        // 오른쪽 공백 (뒤로가기 버튼과 크기 맞추기)
        else Spacer(modifier = Modifier.size(40.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun TopBarArrowPreview() {
    GogomaTheme {
        TopBarArrow(title = "결제하기", onBackClick = {}, refreshAction = { println("refresh") })
    }
}
