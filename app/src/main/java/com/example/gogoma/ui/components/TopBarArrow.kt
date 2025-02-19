package com.example.gogoma.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gogoma.R
import com.example.gogoma.theme.GogomaTheme
import com.example.gogoma.theme.PartialSansKR
import com.example.gogoma.theme.Pretendard

@Composable
fun TopBarArrow(
    title: String? = null,
    isDisplay: Boolean = false,
    onBackClick: () -> Unit,
    refreshAction: (()->Unit)? = null
) {
    val topInset = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(45.dp + topInset)
            .background(MaterialTheme.colorScheme.background)
            .padding(start = 20.dp, end = 20.dp, top = topInset),
        horizontalArrangement = Arrangement.spacedBy(15.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 뒤로가기 버튼
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.size(24.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.arrow_back),
                contentDescription = "Back Arrow",
                tint = Color(0xFFCFCFCF),
                modifier = Modifier.size(20.dp)
            )
        }

        //타이틀 스크롤 가능
        Box(
            modifier = Modifier
                .weight(1f)
                .horizontalScroll(rememberScrollState()),
            contentAlignment = if (isDisplay) Alignment.CenterStart else Alignment.Center
        ) {
            title?.let {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    color = Color(0xFF222222),
                    fontFamily = if (isDisplay) PartialSansKR else Pretendard,
                    fontWeight = FontWeight.Bold,
                )
            }
        }

        if (refreshAction != null) {
            IconButton(
                onClick = refreshAction,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.icon_refresh),
                    contentDescription = "Refresh",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(22.dp)
                )
            }
        } else if(!isDisplay) {
            // 오른쪽 공백 (뒤로가기 버튼과 크기 맞추기)
            Spacer(modifier = Modifier.size(24.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TopBarArrowPreview() {
    GogomaTheme {
        TopBarArrow(title = "결제하기", onBackClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun TopBarArrowPreview2() {
    GogomaTheme {
        TopBarArrow(title = "친구", onBackClick = {}, refreshAction = { println("refresh") })
    }
}

@Preview(showBackground = true)
@Composable
fun TopBarArrowPreview3() {
    GogomaTheme {
        TopBarArrow(title = "서울 마라톤 2022", isDisplay = true, onBackClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun TopBarArrowPreview4() {
    GogomaTheme {
        TopBarArrow(title = "서울 마라톤 2022 asdfsdfafawefawfasdf", isDisplay = true, onBackClick = {}, refreshAction = { })
    }
}

@Preview(showBackground = true)
@Composable
fun TopBarArrowPreview5() {
    GogomaTheme {
        TopBarArrow(onBackClick = {})
    }
}
