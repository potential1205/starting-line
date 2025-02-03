package com.example.gogoma.ui.screens

import android.util.Log
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
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PaymentScreen(
    navController: NavController,
    marathonId: Int?,
    viewModel: PaymentViewModel
) {
    val savedStateHandle = navController.previousBackStackEntry?.savedStateHandle
    var marathonDetail by remember { mutableStateOf<MarathonDetailResponse?>(null) }

    val gson = remember { Gson() } // ✅ Gson 인스턴스


    // ✅ 마라톤 상세 데이터를 가져오기
    LaunchedEffect(marathonId) {
        marathonDetail = savedStateHandle?.get<MarathonDetailResponse>("marathonDetail_$marathonId")
        println("✅ 결제 페이지에서 받은 마라톤 정보: $marathonDetail")
    }

    val selectedPayment by viewModel.selectedPayment.collectAsState()
    var isAgreementChecked by remember { mutableStateOf(false) } // ✅ 기본값 false
    val selectedAddress by viewModel.selectedAddress.collectAsState()

    var selectedPrice by remember { mutableStateOf(0) }
    var selectedOption by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = { TopBarArrow(title = "결제하기", onBackClick = { navController.popBackStack() }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            // ✅ 배송지 및 사이즈 선택
            AddressSizeSelection(
                selectedAddress = selectedAddress,
                viewModel = viewModel,
                onAddressClick = { navController.navigate("addressSelection") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ✅ 참가 종목 선택 (가격 반영)
            marathonDetail?.let { data ->
                val courseOptions = mutableListOf<Pair<String, Int>>()

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

                if (courseOptions.isNotEmpty()) {
                    SectionWithRadioButtons(
                        title = "참가 종목",
                        options = courseOptions.map { it.first },
                        selectedOption = selectedOption ?: "",
                        onOptionSelected = { selected ->
                            selectedOption = selected
                            viewModel.updateSelectedDistance(selected)
                            selectedPrice = courseOptions.find { it.first == selected }?.second ?: 0
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ✅ 개인정보 동의
            AgreementItem(
                text = "개인정보 수집 및 이용 안내",
                isChecked = isAgreementChecked,
                onCheckedChange = { checked -> isAgreementChecked = checked },
                onViewClicked = { /* 약관 보기 */ }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ✅ 결제 수단 선택
            SectionWithRadioButtons(
                title = "결제 수단",
                options = listOf("카카오페이", "토스", "무통장 입금"),
                selectedOption = selectedPayment,
                onOptionSelected = { paymentMethod -> viewModel.updateSelectedPayment(paymentMethod) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ✅ 결제 금액 표시
            PaymentAmount(amount = selectedPrice)

            Spacer(modifier = Modifier.height(16.dp))

            // ✅ 결제 완료 시 신청 내역 생성 및 JSON 변환 후 전달
            BottomBarButton(
                text = "결제하기",
                backgroundColor = if (isAgreementChecked && selectedOption != null) BrandColor1 else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                textColor = if (isAgreementChecked && selectedOption != null) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                onClick = {
                    if (isAgreementChecked && selectedOption != null) {
                        val regist = marathonDetail?.let { detail ->
                            val currentDate = SimpleDateFormat("yy.MM.dd", Locale.KOREA).format(Date())

                            // ✅ `date` 값을 "yyyy-MM-dd" → "yyyy.MM.dd"로 변환
                            val rawDate = detail.marathon.raceStartTime.substring(0, 10) // yyyy-MM-dd
                            val formattedDate = try {
                                val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
                                val outputFormat = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA)
                                val date = inputFormat.parse(rawDate)
                                outputFormat.format(date) // yyyy.MM.dd 변환
                            } catch (e: Exception) {
                                Log.e("PaymentScreen", "❌ 날짜 변환 실패: ${e.message}")
                                rawDate // 변환 실패 시 원본 유지
                            }

                            // ✅ 정규식을 사용하여 거리 값만 추출 (예: "10km", "하프", "풀코스")
                            val distanceOnly = selectedOption?.split(" - ")?.firstOrNull() ?: ""

                            Regist(
                                registrationDate = currentDate,
                                title = detail.marathon.title,
                                date = formattedDate, // ✅ 변환된 날짜 적용
                                distance = distanceOnly
                            )
                        }

                        val registJson = gson.toJson(regist)
                        Log.d("PaymentScreen", "✅ 저장할 Regist JSON: $registJson")

                        when (selectedPayment) {
                            "카카오페이" -> {
                                // **✅ NavController에 데이터 직접 전달**
                                navController.navigate("paymentSuccess/$registJson")
                            }
                            "토스" -> navController.navigate("paymentFailure")
                        }
                    }
                },
                enabled = isAgreementChecked && selectedOption != null
            )

        }
    }
}

