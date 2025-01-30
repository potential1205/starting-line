package com.example.gogoma.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gogoma.theme.BrandColor1
import com.example.gogoma.ui.components.*
import com.example.gogoma.viewmodel.PaymentViewModel

@Composable
fun PaymentScreen(
    navController: NavController,
    viewModel: PaymentViewModel // ViewModel을 추가하여 모든 상태를 ViewModel에서 관리
) {
    val selectedDistance by viewModel.selectedDistance.collectAsState()
    val selectedPayment by viewModel.selectedPayment.collectAsState()
    val isAgreementChecked by viewModel.isAgreementChecked.collectAsState()
    val selectedSize by viewModel.selectedSize.collectAsState()
    val selectedAddress by viewModel.selectedAddress.collectAsState()

    var showSizeDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopBarArrow(title = "결제하기", onBackClick = { navController.popBackStack() }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            // 배송지 및 사이즈 선택
            AddressSizeSelection(
                selectedAddress = selectedAddress,
                viewModel = viewModel, // ✅ ViewModel 전달
                onAddressClick = { navController.navigate("addressSelection") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 참가 종목 선택
            SectionWithRadioButtons(
                title = "참가 종목",
                options = listOf("5km", "10km", "하프", "풀"),
                selectedOption = selectedDistance,
                onOptionSelected = { viewModel.updateSelectedDistance(it) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 개인정보 동의
            AgreementItem(
                text = "개인정보 수집 및 이용 안내",
                isChecked = isAgreementChecked,
                onCheckedChange = { viewModel.updateAgreementChecked(it) },
                onViewClicked = { /* 약관 보기 */ }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 결제 수단 선택
            SectionWithRadioButtons(
                title = "결제 수단",
                options = listOf("카카오페이", "토스", "무통장 입금"),
                selectedOption = selectedPayment,
                onOptionSelected = { viewModel.updateSelectedPayment(it) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 결제 금액 표시
            PaymentAmount(amount = 50000)

            Spacer(modifier = Modifier.height(16.dp))

            // 결제하기 버튼
            BottomBarButton(
                text = "결제하기",
                backgroundColor = if (isAgreementChecked) BrandColor1 else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                textColor = if (isAgreementChecked) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                onClick = {
                    if (isAgreementChecked) {
                        // 결제 로직 추가
                    }
                },
                enabled = isAgreementChecked
            )
        }
    }

    // 사이즈 선택 모달
    if (showSizeDialog) {
        SizeSelectionDialog(
            viewModel = viewModel,
            onDismiss = { showSizeDialog = false }
        )
    }
}
