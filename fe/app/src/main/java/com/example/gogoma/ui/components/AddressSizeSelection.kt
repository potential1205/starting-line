package com.example.gogoma.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gogoma.data.model.Address
import com.example.gogoma.theme.BrandColor1
import com.example.gogoma.viewmodel.PaymentViewModel

@Composable
fun AddressSizeSelection(
    selectedAddress: Address?,
    viewModel: PaymentViewModel, // ViewModel을 매개변수로 받음
    onAddressClick: () -> Unit
) {
    var showSizeSelectionDialog by remember { mutableStateOf(false) }
    val selectedSize by viewModel.selectedSize.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = selectedAddress?.name ?: "배송지 선택 필요",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = selectedAddress?.address ?: "주소를 선택해주세요",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = selectedAddress?.detailAddress ?: "상세주소를 입력해주세요",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = selectedAddress?.phone ?: "전화번호 없음",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Button(
                onClick = onAddressClick,
                colors = ButtonDefaults.buttonColors(containerColor = BrandColor1),
                modifier = Modifier.wrapContentHeight().padding(start = 8.dp)
            ) {
                Text(text = "배송지 변경", fontSize = 14.sp, color = MaterialTheme.colorScheme.onPrimary)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 옷 사이즈 정보 (클릭 시 모달 열림)
        Text(
            text = "옷 사이즈: $selectedSize",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .clickable { showSizeSelectionDialog = true }
                .padding(vertical = 8.dp)
        )

        // 사이즈 선택 모달
        if (showSizeSelectionDialog) {
            SizeSelectionDialog(
                viewModel = viewModel,
                onDismiss = { showSizeSelectionDialog = false }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddressSizeSelectionPreview() {
    val sampleAddress = Address(
        id = "1",
        name = "홍길동",
        address = "서울특별시 영등포구 선유로 00 현대아파트",
        detailAddress = "101동 202호",
        phone = "010-0000-0000",
        isDefault = true
    )

    val mockViewModel = PaymentViewModel() // Preview에서 직접 ViewModel 생성

    AddressSizeSelection(
        selectedAddress = sampleAddress,
        viewModel = mockViewModel, // Preview에서 사용 가능하게 변경
        onAddressClick = {}
    )
}
