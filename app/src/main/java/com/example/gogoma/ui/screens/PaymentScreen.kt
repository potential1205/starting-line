package com.example.gogoma.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gogoma.data.dto.KakaoPayReadyRequest
import com.example.gogoma.data.dto.UserMarathonSearchDto
import com.example.gogoma.theme.BrandColor1
import com.example.gogoma.ui.components.*
import com.example.gogoma.viewmodel.PaymentViewModel
import com.example.gogoma.data.model.MarathonDetailResponse
import com.example.gogoma.viewmodel.BottomSheetViewModel
import com.example.gogoma.viewmodel.UserViewModel
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PaymentScreen(
    navController: NavController,
    marathonId: Int?,
    paymentViewModel: PaymentViewModel,
    userViewModel: UserViewModel,
    bottomSheetViewModel: BottomSheetViewModel
) {
    val savedStateHandle = navController.previousBackStackEntry?.savedStateHandle
    var marathonDetail by remember { mutableStateOf<MarathonDetailResponse?>(null) }
    val gson = remember { Gson() }
    val context = LocalContext.current

    LaunchedEffect(marathonId) {
        marathonDetail = savedStateHandle?.get<MarathonDetailResponse>("marathonDetail_$marathonId")
        println("✅ 결제 페이지에서 받은 마라톤 정보: $marathonDetail")
    }

    val selectedPayment by paymentViewModel.selectedPayment.collectAsState()
    var isAgreementChecked by remember { mutableStateOf(false) }
    val selectedAddress by paymentViewModel.selectedAddress.collectAsState()

    var selectedPrice by remember { mutableStateOf(0) }
    var selectedOption by remember { mutableStateOf<String?>(null) }
    val kakaoPayReadyResponse by paymentViewModel.kakaoPayReadyResponse.collectAsState()

    val regist = marathonDetail?.let { detail ->
        val currentDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.KOREA).format(Date())

        val rawDate = detail.marathon.raceStartTime.substring(0, 10)
        val formattedDate = try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
            val outputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.KOREA)
            val date = inputFormat.parse(rawDate)
            outputFormat.format(date)
        } catch (e: Exception) {
            Log.e("PaymentScreen", "❌ 날짜 변환 실패: ${e.message}")
            rawDate
        }

        val distanceOnly = selectedOption?.split(" - ")?.firstOrNull()?.filter { it.isDigit() }?.toIntOrNull() ?: 0

        UserMarathonSearchDto (
            paymentDateTime = currentDate,
            marathonTitle = detail.marathon.title,
            raceStartDateTime = formattedDate,
            marathonType = distanceOnly,
            userMarathonId = detail.marathon.id
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
                        paymentViewModel.updateSelectedPayment("카카오페이")

                        if (isAgreementChecked && selectedOption != null) {
                            val regist = marathonDetail?.let { detail ->
                                val currentDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.KOREA).format(Date())
                                val rawDate = detail.marathon.raceStartTime.substring(0, 10)
                                val formattedDate = try {
                                    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
                                    val outputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.KOREA)
                                    val date = inputFormat.parse(rawDate)
                                    outputFormat.format(date)
                                } catch (e: Exception) {
                                    Log.e("PaymentScreen", "❌ 날짜 변환 실패: ${e.message}")
                                    rawDate
                                }
                                val distanceOnly = selectedOption?.split(" - ")?.firstOrNull()?.filter { it.isDigit() }?.toIntOrNull() ?: 0

                                UserMarathonSearchDto (
                                    paymentDateTime = currentDate,
                                    marathonTitle = detail.marathon.title,
                                    raceStartDateTime = formattedDate,
                                    marathonType = distanceOnly,
                                    userMarathonId = detail.marathon.id
                                )
                            }

                            val registJson = gson.toJson(regist)
                            Log.d("PaymentScreen", "✅ 저장할 Regist JSON: $registJson")

                            when (selectedPayment) {
                                "카카오페이" -> {
                                    paymentViewModel.requestKakaoPayReady(
                                        KakaoPayReadyRequest(
                                            orderId = marathonId.toString(),
                                            itemName = marathonDetail?.marathon?.title ?: "마라톤 참가권",
                                            totalAmount = selectedPrice.toString()
                                        ),
                                        context
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
                    navController = navController,
                    userViewModel = userViewModel,
                    paymentViewModel = paymentViewModel,
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
                            ifFormatNeed = true,
                            title = "참가 종목",
                            options = courseOptions.map { it.first },
                            selectedOption = selectedOption ?: "",
                            onOptionSelected = { selected ->
                                selectedOption = selected
                                paymentViewModel.updateSelectedDistance(selected)
                                selectedPrice = courseOptions.find { it.first == selected }?.second ?: 0
                                paymentViewModel.updateSelectedPrice(selectedPrice)
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
                    onViewClicked = { bottomSheetViewModel.showBottomSheet("privacyPolicy") }
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
