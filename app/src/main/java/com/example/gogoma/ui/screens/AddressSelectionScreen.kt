package com.example.gogoma.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gogoma.theme.GogomaTheme

// 배송지 데이터 모델
data class Address(
    val name: String,
    val address: String,
    val phone: String,
    val isDefault: Boolean = false
)

@Composable
fun AddressSelectionScreen(
    addressList: List<Address>,
    selectedAddress: Address?,
    onAddressSelected: (Address) -> Unit,
    onAddNewAddress: () -> Unit
) {
    var selected by remember { mutableStateOf(selectedAddress) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "배송지",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // 신규 배송지 추가 버튼
        Button(
            onClick = onAddNewAddress,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
        ) {
            Text("+ 신규 배송지 추가")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(addressList) { address ->
                AddressItem(
                    address = address,
                    isSelected = address == selected,
                    onSelect = {
                        selected = it
                        onAddressSelected(it)
                    }
                )
            }
        }
    }
}

@Composable
fun AddressItem(address: Address, isSelected: Boolean, onSelect: (Address) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect(address) }
            .padding(12.dp)
            .background(if (isSelected) Color(0xFFDFF6FF) else Color.White),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = { onSelect(address) },
            colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF4CAF50))
        )
        Column(modifier = Modifier.padding(start = 8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = address.name, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                if (address.isDefault) {
                    Text(
                        text = " 기본주소",
                        fontSize = 14.sp,
                        color = Color.Blue,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
            Text(text = address.address, fontSize = 14.sp, color = Color.Gray)
            Text(text = address.phone, fontSize = 14.sp, color = Color.Gray)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddressSelectionPreview() {
    GogomaTheme {
        AddressSelectionScreen(
            addressList = listOf(
                Address("홍길동", "서울특별시 영등포구 선유로 00 현대아파트", "010-0000-0000", isDefault = true),
                Address("김이름", "서울특별시 강남구 테헤란로 00", "010-1234-5678"),
                Address("박철수", "부산광역시 해운대구 달맞이길 00", "010-9876-5432")
            ),
            selectedAddress = null,
            onAddressSelected = {},
            onAddNewAddress = {}
        )
    }
}