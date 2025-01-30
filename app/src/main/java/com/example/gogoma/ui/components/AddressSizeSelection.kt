package com.example.gogoma.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gogoma.theme.BrandColor1
import com.example.gogoma.theme.GogomaTheme

@Composable
fun AddressSizeSelection() {
    var showAddressDialog by remember { mutableStateOf(false) }
    var showSizeDialog by remember { mutableStateOf(false) }

    var selectedAddress by remember { mutableStateOf("주소 실제로는 데이터 받아와서 보여줌") }
    var selectedSize by remember { mutableStateOf("옷 사이즈 실제로는 데이터 받아와서 보여줌") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // 상단 정보 (이름 + 전화번호 + 배송지 변경 버튼)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier.weight(1f) // 왼쪽 영역 확장
            ) {
                Text(
                    text = "이름",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp)) // 이름과 전화번호 간격
                Text(
                    text = "010-0000-0000",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // 우측 상단 "배송지 변경" 버튼 (이름 높이보다 작은 고정값 사용)
            Button(
                onClick = { showAddressDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = BrandColor1),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .height(40.dp) // 버튼 높이를 줄임
                    .align(Alignment.Top)
            ) {
                Text(text = "배송지 변경", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 주소 정보 (클릭 시 배송지 변경 다이얼로그 호출)
        Text(
            text = selectedAddress,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.clickable { showAddressDialog = true }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 옷 사이즈 정보
        Text(
            text = selectedSize,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.clickable { showSizeDialog = true }
        )
    }

    // 배송지 선택 모달
    if (showAddressDialog) {
        AddressSelectionDialog(
            currentAddress = selectedAddress,
            onAddressSelected = { newAddress ->
                selectedAddress = newAddress
                showAddressDialog = false
            },
            onDismiss = { showAddressDialog = false }
        )
    }

    // 사이즈 선택 모달
    if (showSizeDialog) {
        SizeSelectionDialog(
            currentSize = selectedSize,
            onSizeSelected = { newSize ->
                selectedSize = newSize
                showSizeDialog = false
            },
            onDismiss = { showSizeDialog = false }
        )
    }
}

// **배송지 선택 모달**
@Composable
fun AddressSelectionDialog(
    currentAddress: String,
    onAddressSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val addressList = listOf(
        "서울특별시 강남구 테헤란로 123",
        "경기도 성남시 분당구 판교로 456",
        "부산광역시 해운대구 해운대로 789"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        title = { Text("배송지 선택") },
        text = {
            Column {
                addressList.forEach { address ->
                    Text(
                        text = address,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onAddressSelected(address) }
                            .padding(8.dp)
                    )
                }
            }
        }
    )
}

// **사이즈 선택 모달**
@Composable
fun SizeSelectionDialog(
    currentSize: String,
    onSizeSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val sizeList = listOf("85", "90", "95", "100", "105")

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        title = { Text("사이즈 선택") },
        text = {
            Column {
                sizeList.forEach { size ->
                    Text(
                        text = size,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSizeSelected(size) }
                            .padding(8.dp)
                    )
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun AddressSizeSelectionPreview() {
    GogomaTheme {
        AddressSizeSelection()
    }
}
