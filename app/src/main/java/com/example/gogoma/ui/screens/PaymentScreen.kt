package com.example.gogoma.ui.screens

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gogoma.data.dto.KakaoPayReadyRequest
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
    val gson = remember { Gson() }

    LaunchedEffect(marathonId) {
        marathonDetail = savedStateHandle?.get<MarathonDetailResponse>("marathonDetail_$marathonId")
        println("✅ 결제 페이지에서 받은 마라톤 정보: $marathonDetail")
    }

    val selectedPayment by viewModel.selectedPayment.collectAsState()
    var isAgreementChecked by remember { mutableStateOf(false) }
    val selectedAddress by viewModel.selectedAddress.collectAsState()

    var selectedPrice by remember { mutableStateOf(0) }
    var selectedOption by remember { mutableStateOf<String?>(null) }
    val kakaoPayReadyResponse by viewModel.kakaoPayReadyResponse.collectAsState()

    val regist = marathonDetail?.let { detail ->
        val currentDate = SimpleDateFormat("yy.MM.dd", Locale.KOREA).format(Date())

        val rawDate = detail.marathon.raceStartTime.substring(0, 10)
        val formattedDate = try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
            val outputFormat = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA)
            val date = inputFormat.parse(rawDate)
            outputFormat.format(date)
        } catch (e: Exception) {
            Log.e("PaymentScreen", "❌ 날짜 변환 실패: ${e.message}")
            rawDate
        }

        val distanceOnly = selectedOption?.split(" - ")?.firstOrNull() ?: ""

        Regist(
            registrationDate = currentDate,
            title = detail.marathon.title,
            date = formattedDate,
            distance = distanceOnly
        )
    }

    Scaffold(
        topBar = { TopBarArrow(title = "결제하기", onBackClick = { navController.popBackStack() }) },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(16.dp)
            ) {
                PaymentAmount(amount = selectedPrice)
                Spacer(modifier = Modifier.height(8.dp))
                BottomBarButton(
                    text = "결제하기",
                    backgroundColor = if (isAgreementChecked && selectedOption != null) BrandColor1 else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                    textColor = if (isAgreementChecked && selectedOption != null) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    onClick = {
                        if (isAgreementChecked && selectedOption != null) {
                            val regist = marathonDetail?.let { detail ->
                                val currentDate = SimpleDateFormat("yy.MM.dd", Locale.KOREA).format(Date())
                                val rawDate = detail.marathon.raceStartTime.substring(0, 10)
                                val formattedDate = try {
                                    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
                                    val outputFormat = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA)
                                    val date = inputFormat.parse(rawDate)
                                    outputFormat.format(date)
                                } catch (e: Exception) {
                                    Log.e("PaymentScreen", "❌ 날짜 변환 실패: ${e.message}")
                                    rawDate
                                }
                                val distanceOnly = selectedOption?.split(" - ")?.firstOrNull() ?: ""

                                Regist(
                                    registrationDate = currentDate,
                                    title = detail.marathon.title,
                                    date = formattedDate,
                                    distance = distanceOnly
                                )
                            }

                            val registJson = gson.toJson(regist)
                            Log.d("PaymentScreen", "✅ 저장할 Regist JSON: $registJson")

                            when (selectedPayment) {
                                "카카오페이" -> {
                                    viewModel.requestKakaoPayReady(
                                        KakaoPayReadyRequest(
                                            userId = "1", // 추후 실제 아이디 받아오게끔 수정해야 함
                                            orderId = marathonId.toString(),
                                            itemName = marathonDetail?.marathon?.title ?: "마라톤 참가권",
                                            totalAmount = selectedPrice.toString()
                                        )
                                    )
                                }
                                "토스" -> navController.navigate("paymentFailure")
                            }
                        }
                    },
                    enabled = isAgreementChecked && selectedOption != null
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            item {
                AddressSizeSelection(
                    selectedAddress = selectedAddress,
                    viewModel = viewModel,
                    onAddressClick = { navController.navigate("addressSelection") }
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            item {
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
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            item {
                AgreementItem(
                    text = "개인정보 수집 및 이용 안내",
                    isChecked = isAgreementChecked,
                    onCheckedChange = { checked -> isAgreementChecked = checked },
                    onViewClicked = { }
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            item {
                SectionWithRadioButtons(
                    title = "결제 수단",
                    options = listOf("카카오페이", "토스", "무통장 입금"),
                    selectedOption = selectedPayment,
                    onOptionSelected = { paymentMethod -> viewModel.updateSelectedPayment(paymentMethod) }
                )
            }
        }
    }

    LaunchedEffect(kakaoPayReadyResponse) {
        kakaoPayReadyResponse?.let { response ->
            val paymentUrl = response.next_redirect_mobile_url ?: ""
            val registJson = gson.toJson(regist)

            navController.currentBackStackEntry?.savedStateHandle?.set("paymentUrl", paymentUrl)
            navController.currentBackStackEntry?.savedStateHandle?.set("registJson", registJson)

            navController.navigate("paymentWebViewScreen")
        }
    }
}
