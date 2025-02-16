package com.example.gogoma.ui.components.bottomsheets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalConfiguration

@Composable
fun BottomSheet(
    isVisible : Boolean,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit,
){
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val maxHeight = screenHeight * 0.7f // 화면 세로 크기의 70%

    if(isVisible){
        Box( //까만 바탕
            modifier = Modifier
                .fillMaxSize() //꽉 채움
                .background(Color.Black.copy(alpha = 0.7f))
                .clickable { onDismiss() }, //배경 클릭 시 닫힘
            contentAlignment = Alignment.BottomCenter //아래로 정렬
        ){
            //애니메이션 효과
            AnimatedVisibility(
                visible = isVisible,
                enter = androidx.compose.animation.expandVertically(animationSpec = tween(300)),
                exit = androidx.compose.animation.shrinkVertically(animationSpec = tween(300))
            ) {
                //실제 바텀 시트(하얀 부분)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 660.dp, max = maxHeight ) // 최소 660dp, 최대 디스플레이 70% 높이
                        .background(
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp)
                        )
                        .padding(16.dp)
                        .clickable (
                            onClick = {},  // 클릭 이벤트를 빈 함수로 설정하여 아무 일도 일어나지 않도록 함
                            indication = null, // 클릭 효과 제거
                            interactionSource = remember { MutableInteractionSource() } // 클릭 이벤트 전파 방지
                        ),
                    contentAlignment = Alignment.Center
                ){
                    content() //안에 들어갈 내용
                }
            }
        }
    }
}

@Preview
@Composable
fun BottomSheetPreview() {
    var showSheet by remember { mutableStateOf(true) }
    BottomSheet(isVisible = showSheet, onDismiss = { showSheet = false }) {
        //content 예시
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("바텀시트 확인")
        }
    }
}