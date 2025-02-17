package com.example.gogoma.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gogoma.R

@Composable
fun TopBarClose(
    iconColor: Color = MaterialTheme.colorScheme.onBackground,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(45.dp)
            .padding(horizontal = 21.dp, vertical = 3.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        // 뒤로가기 버튼
        IconButton(
            onClick = onCloseClick,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_close),
                contentDescription = "Close icon",
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun TopBarClosePreview(){
    TopBarClose(onCloseClick = {})
}