package com.example.gogoma.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gogoma.R
import com.example.gogoma.theme.BrandColor1
import com.example.gogoma.theme.GogomaTheme

@Composable
fun AgreementItem(
    text: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onViewClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!isChecked) } // 클릭 시 상태 변경
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 체크박스 아이콘
        IconButton(
            onClick = { onCheckedChange(!isChecked) },
            modifier = Modifier.size(24.dp)
        ) {
            Icon(
                painter = painterResource(
                    id = if (isChecked) R.drawable.checkbox_checked else R.drawable.checkbox_unchecked
                ),
                contentDescription = "Checkbox",
                tint = Color.Unspecified
            )
        }

        // 텍스트
        Text(
            text = text,
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier.weight(1f) // 남은 공간을 차지해서 정렬
        )

        // [보기] 버튼
        Text(
            text = "[보기]",
            fontSize = 16.sp,
            color = BrandColor1,
            modifier = Modifier
                .clickable { onViewClicked() } // 클릭 가능
                .padding(end = 8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AgreementItemPreview() {
    var checked by remember { mutableStateOf(true) }

    GogomaTheme {
        AgreementItem(
            text = "선택하면 이렇게 표시됨 + 약관 두번째 스타일",
            isChecked = checked,
            onCheckedChange = { checked = it },
            onViewClicked = { /* 클릭 이벤트 */ }
        )
    }
}
