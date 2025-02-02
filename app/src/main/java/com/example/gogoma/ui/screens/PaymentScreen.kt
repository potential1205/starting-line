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
import com.example.gogoma.data.model.MarathonDetailResponse

@Composable
fun PaymentScreen(
    navController: NavController,
    marathonId: Int?,
    viewModel: PaymentViewModel
) {
    val savedStateHandle = navController.previousBackStackEntry?.savedStateHandle
    var marathonDetail by remember { mutableStateOf<MarathonDetailResponse?>(null) }

    // 마라톤 상세 데이터를 가져오기
    LaunchedEffect(marathonId) {
        marathonDetail = savedStateHandle?.get<MarathonDetailResponse>("marathonDetail_$marathonId")
        println("결제 페이지에서 받은 마라톤 정보: $marathonDetail")
    }

    val selectedPayment by viewModel.selectedPayment.collectAsState()
    val isAgreementChecked by viewModel.isAgreementChecked.collectAsState()
    val selectedAddress by viewModel.selectedAddress.collectAsState()

    var selectedPrice by remember { mutableStateOf(0) } // 선택한 종목의 결제 금액
    var selectedOption by remember { mutableStateOf<String?>(null) } // 초기 상태는 선택 없음

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
                viewModel = viewModel,
                onAddressClick = { navController.navigate("addressSelection") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 참가 종목 선택 (가격 반영)
            marathonDetail?.let { data ->
                val courseOptions = mutableListOf<Pair<String, Int>>() // 종목명 - 가격 리스트

                data.marathonTypeList.groupBy { it.courseType }.forEach { (courseType, types) ->
                    types.forEach { type ->
                        val label = if (type.etc.isNotEmpty()) {
                            "$courseType - ${type.price}원 (${type.etc})"
                        } else {
                            "$courseType - ${type.price}원"
                        }
                        courseOptions.add(label to (type.price?.toIntOrNull() ?: 0))
                    }
                }

                println("생성된 종목 옵션: $courseOptions") // 디버깅 로그

                if (courseOptions.isNotEmpty()) {
                    SectionWithRadioButtons(
                        title = "참가 종목",
                        options = courseOptions.map { it.first },
                        selectedOption = selectedOption ?: "", // Null 대신 빈 문자열 사용
                        onOptionSelected = { selected ->
                            println("선택한 종목: $selected") // 디버깅
                            selectedOption = selected
                            viewModel.updateSelectedDistance(selected)
                            selectedPrice = courseOptions.find { it.first == selected }?.second ?: 0
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 개인정보 동의
            AgreementItem(
                text = "개인정보 수집 및 이용 안내",
                isChecked = isAgreementChecked,
                onCheckedChange = { checked -> viewModel.updateAgreementChecked(checked) },
                onViewClicked = { /* 약관 보기 */ }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 결제 수단 선택
            SectionWithRadioButtons(
                title = "결제 수단",
                options = listOf("카카오페이", "토스", "무통장 입금"),
                selectedOption = selectedPayment,
                onOptionSelected = { paymentMethod ->
                    viewModel.updateSelectedPayment(paymentMethod)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            println("결제 금액 확인: 선택한 종목 = $selectedOption, 가격 = $selectedPrice") // 결제 금액 디버깅

            // 결제 금액 표시 (선택한 종목의 가격 적용)
            PaymentAmount(amount = selectedPrice)

            Spacer(modifier = Modifier.height(16.dp))

            // 결제하기 버튼
            BottomBarButton(
                text = "결제하기",
                backgroundColor = if (isAgreementChecked) BrandColor1 else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                textColor = if (isAgreementChecked) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                onClick = {
                    if (isAgreementChecked) {
                        when (selectedPayment) {
                            "카카오페이" -> navController.navigate("paymentSuccess")
                            "토스" -> navController.navigate("paymentFailure")
                        }
                    }
                },
                enabled = isAgreementChecked
            )
        }
    }
}
