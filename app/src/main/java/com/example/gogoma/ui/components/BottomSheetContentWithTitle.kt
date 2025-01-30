package com.example.gogoma.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gogoma.R

@Composable
fun BottomSheetContentWithTitle(
    title: String, //제목
    headerLeftContent : @Composable (() -> Unit)? = null, //뒤로 버튼 등 제목 좌측 요소
    headerRightContent : @Composable (() -> Unit)? = null, //제목 우측 요소
    content: @Composable () -> Unit //내용
){
    Column(
        modifier = Modifier
            .padding(start = 20.dp, top = 15.dp, end = 20.dp, bottom = 15.dp)
            .fillMaxHeight()
    ) {
        //헤더
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // 왼쪽 콘텐츠
            Box(modifier = Modifier
                .width(48.dp),
                contentAlignment = Alignment.Center
            ) {
                headerLeftContent?.let {
                    it()
                }
            }

            // 제목
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )

            // 오른쪽 콘텐츠
            Box(modifier = Modifier
                .width(48.dp),
                contentAlignment = Alignment.Center
            ) {
                headerRightContent?.let {
                    it()
                }
            }
        }

        // 제목과 내용 사이의 간격
        Spacer(modifier = Modifier.height(17.5.dp))

        content()
    }
}

@Preview
@Composable
fun BottomSheetWithTitleAndIconsPreview() {
    BottomSheetContentWithTitle(
        title = "이것은 제목입니다",
        headerLeftContent = {
            Icon(painter = painterResource(id = R.drawable.arrow_back), contentDescription = "왼쪽 아이콘")
        },
        headerRightContent = {
            IconButton (onClick = { /* 오른쪽 버튼 클릭 시 동작 */ }) {
                Icon(painter = painterResource(id = R.drawable.icon_search), contentDescription = "오른쪽 아이콘")
            }
        }
    ) {
        Column {
            Text("내용 부분을 자유롭게 구성할 수 있습니다.")
            Spacer(modifier = Modifier.height(8.dp))
            Text("다양한 정보를 추가할 수 있어요.")
        }
    }
}

@Preview
@Composable
fun BottomSheetWithTitleAndIconsPreview2() {
    BottomSheetContentWithTitle(
        title = "이것은 제목입니다",
        headerLeftContent = {
            Icon(painter = painterResource(id = R.drawable.arrow_back), contentDescription = "왼쪽 아이콘")
        }
    ) {
        Column {
            Text("내용 부분을 자유롭게 구성할 수 있습니다.")
            Spacer(modifier = Modifier.height(8.dp))
            Text("다양한 정보를 추가할 수 있어요.")
        }
    }
}

@Preview
@Composable
fun BottomSheetWithTitleAndIconsPreview3() {
    BottomSheetContentWithTitle(
        title = "이것은 제목입니다",
        headerRightContent = {
            IconButton (onClick = { /* 오른쪽 버튼 클릭 시 동작 */ }) {
                Icon(painter = painterResource(id = R.drawable.icon_search), contentDescription = "오른쪽 아이콘")
            }
        }
    ) {
        Column {
            Text("내용 부분을 자유롭게 구성할 수 있습니다.")
            Spacer(modifier = Modifier.height(8.dp))
            Text("다양한 정보를 추가할 수 있어요.")
        }
    }
}