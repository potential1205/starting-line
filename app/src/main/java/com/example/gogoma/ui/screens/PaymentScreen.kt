package com.example.gogoma.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.gogoma.theme.BrandColor1
import com.example.gogoma.ui.components.*
import com.example.gogoma.theme.GogomaTheme

@Composable
fun PaymentScreen(navController: NavController) {
    var selectedDistance by remember { mutableStateOf("5km") }
    var selectedPayment by remember { mutableStateOf("카카오페이") }
    var isAgreementChecked by remember { mutableStateOf(false) }
    var totalAmount by remember { mutableStateOf(50000) } // 결제 금액

    Scaffold(
        topBar = { TopBarArrow(title = "결제하기", onBackClick = { navController.popBackStack() }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            // 주소 및 사이즈 선택
            AddressSizeSelection()

            Spacer(modifier = Modifier.height(16.dp))

            // 참가 종목 선택 (라디오 버튼)
            SectionWithRadioButtons(
                title = "참가 종목",
                options = listOf("5km", "10km", "하프", "풀"),
                selectedOption = selectedDistance,
                onOptionSelected = { selectedDistance = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 개인정보 동의
            AgreementItem(
                text = "개인정보 수집 및 이용 안내",
                isChecked = isAgreementChecked,
                onCheckedChange = { isAgreementChecked = it }, // 상태 업데이트
                onViewClicked = { /* TODO: 약관 보기 페이지 연결 */ }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 결제 수단 선택
            SectionWithRadioButtons(
                title = "결제 수단",
                options = listOf("카카오페이", "토스", "무통장 입금"),
                selectedOption = selectedPayment,
                onOptionSelected = { selectedPayment = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 결제 금액 표시
            PaymentAmount(amount = totalAmount)

            Spacer(modifier = Modifier.height(16.dp))

            // 결제하기 버튼 (약관 동의 여부에 따라 활성/비활성)
            BottomBarButton(
                text = "결제하기",
                backgroundColor = if (isAgreementChecked) BrandColor1 else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f), // 동의 X 시 회색 처리
                textColor = if (isAgreementChecked) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                onClick = { if (isAgreementChecked) { /* TODO: 결제 진행 로직 추가 */ } }, // 동의 X 시 클릭 불가
                enabled = isAgreementChecked // 비활성화 적용
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PaymentScreenPreview() {
    GogomaTheme {
        PaymentScreen(navController = rememberNavController())
    }
}
