package com.example.gogoma.ui.components

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.gogoma.R
import com.example.gogoma.data.model.Address
import com.example.gogoma.theme.BrandColor1
import com.example.gogoma.ui.screens.AddressApiActivity
import com.example.gogoma.viewmodel.PaymentViewModel
import com.example.gogoma.viewmodel.UserViewModel

@Composable
fun AddressSizeSelection(
    navController: NavController,
    userViewModel: UserViewModel,
    paymentViewModel: PaymentViewModel, // ViewModel을 매개변수로 받음
) {
    val context = LocalContext.current

    var showSizeSelectionDialog by remember { mutableStateOf(false) }

    val selectedAddress by paymentViewModel.selectedAddress.collectAsState()
    val selectedSize by paymentViewModel.selectedSize.collectAsState()
    
    // AddressApiActivity를 실행하여 주소 검색 결과를 받아오는 launcher 생성
    val addressLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // AddressApiActivity에서 반환한 "data" 값을 roadAddress에 반영
            val addressData = result.data?.getStringExtra("data") ?: ""
            paymentViewModel.updateTmpAddress(addressData)
            navController.navigate("detailAddressSelection")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 0.4.dp, color = Color(0xFFE4E4E4), shape = RoundedCornerShape(size = 16.dp))
            .background(color = MaterialTheme.colorScheme.background, shape = RoundedCornerShape(size = 16.dp))
    ) {
        Row(
            modifier = Modifier
                .clickable {
                    addressLauncher.launch(Intent(context, AddressApiActivity::class.java))
                }
                .padding(horizontal = 20.dp, vertical = 25.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.Top),
            ) {
                val nameText = userViewModel.kakaoUserInfo?.name?.takeIf { it.isNotEmpty() } ?: "배송지 선택 필요"
                val phoneNumberText = userViewModel.kakaoUserInfo?.phoneNumber?.takeIf { it.isNotEmpty() } ?: "전화번호 없음"
                val addressText = selectedAddress?.address?.takeIf { it.isNotEmpty() } ?: "주소를 선택해주세요"
                val detailAddressText = selectedAddress?.detailAddress?.takeIf { it.isNotEmpty() } ?: "상세주소를 입력해주세요"

                Text(
                    text = nameText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = phoneNumberText,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                )
                //주소
                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp, Alignment.Top),
                ) {
                    Text(
                        text = addressText,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = detailAddressText,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Icon(
                painter = painterResource(id = R.drawable.icon_arrow_forward_ios),
                contentDescription = "Back Arrow",
                tint = Color(0xFFCFCFCF),
                modifier = Modifier.size(20.dp)
            )
        }

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = Color(0xFFDDDDDD)
        )

        Row(
            modifier = Modifier
                .clickable { showSizeSelectionDialog = true }
                .padding(horizontal = 20.dp, vertical = 25.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.Top),
            ) {
                // 옷 사이즈 정보 (클릭 시 모달 열림)
                Text(
                    text = "옷 사이즈",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                )

                Text(
                    text = "$selectedSize",
                    fontSize = 11.sp,
                )
            }
            Icon(
                painter = painterResource(id = R.drawable.icon_arrow_forward_ios),
                contentDescription = "Back Arrow",
                tint = Color(0xFFCFCFCF),
                modifier = Modifier.size(20.dp)
            )
        }
    }

    // 사이즈 선택 모달
    if (showSizeSelectionDialog) {
        SizeSelectionDialog(
            viewModel = paymentViewModel,
            onDismiss = { showSizeSelectionDialog = false }
        )
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

    AddressSizeSelection(
        navController = rememberNavController(),
        userViewModel = UserViewModel(),
        paymentViewModel = PaymentViewModel(),
    )
}
