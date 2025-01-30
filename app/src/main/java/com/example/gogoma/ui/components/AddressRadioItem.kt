package com.example.gogoma.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
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
import com.example.gogoma.data.model.Address
import com.example.gogoma.theme.BrandColor1
import com.example.gogoma.theme.GogomaTheme

@Composable
fun AddressRadioItem(
    address: Address,
    isSelected: Boolean,
    onSelect: (Address) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect(address) }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 라디오 버튼 아이콘 (선택 여부에 따라 변경)
        IconButton(
            onClick = { onSelect(address) },
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                painter = painterResource(
                    id = if (isSelected) R.drawable.radio_button_checked
                    else R.drawable.radio_button_default
                ),
                contentDescription = "Radio button for ${address.name}",
                tint = Color.Unspecified
            )
        }

        // 주소 정보
        Column(modifier = Modifier.padding(start = 8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = address.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                if (address.isDefault) {
                    Text(
                        text = " 기본주소",
                        fontSize = 14.sp,
                        color = BrandColor1,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
            Text(text = address.address, fontSize = 14.sp, color = Color.Gray)
            Text(text = address.detailAddress, fontSize = 14.sp, color = Color.Gray)
            Text(text = address.phone, fontSize = 14.sp, color = Color.Gray)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddressRadioItemPreview() {
    GogomaTheme {
        AddressRadioItem(
            address = Address(
                id = "1",
                name = "홍길동",
                address = "서울특별시 영등포구 선유로 00 현대아파트",
                detailAddress = "101동 202호",
                phone = "010-0000-0000",
                isDefault = true
            ),
            isSelected = true,
            onSelect = {}
        )
    }
}
