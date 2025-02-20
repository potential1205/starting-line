package com.example.gogoma.ui.screens

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gogoma.R
import com.example.gogoma.data.dto.KakaoPayReadyRequest
import com.example.gogoma.data.dto.UserMarathonSearchDto
import com.example.gogoma.data.model.ApplyInfoRequest
import com.example.gogoma.theme.BrandColor1
import com.example.gogoma.ui.components.*
import com.example.gogoma.viewmodel.PaymentViewModel
import com.example.gogoma.data.model.MarathonDetailResponse
import com.example.gogoma.utils.TokenManager
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

    // 뒤로 가기 동작 정의
    BackHandler(enabled = bottomSheetViewModel.isBottomSheetVisible) {
        // 모달창이 열려 있을 때 뒤로 가기 버튼 처리
        if (bottomSheetViewModel.isSubPageVisible) {
            // 모달 내에서 페이지가 바뀌었으면 이전 페이지로 돌아가게 처리
            bottomSheetViewModel.goBackToPreviousPage()
        } else {
            // 처음 연 모달 창이라면 모달 닫기
            bottomSheetViewModel.hideBottomSheet()
        }
    }

    val savedStateHandle = navController.previousBackStackEntry?.savedStateHandle
    var marathonDetail by remember { mutableStateOf<MarathonDetailResponse?>(null) }
    val gson = remember { Gson() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        TokenManager.getAccessToken(context)?.let { paymentViewModel.fetchApplyInfo(it) }
    }

    LaunchedEffect(marathonId) {
        marathonDetail = savedStateHandle?.get<MarathonDetailResponse>("marathonDetail_$marathonId")
        println("✅ 결제 페이지에서 받은 마라톤 정보: $marathonDetail")
    }

    val selectedPayment by paymentViewModel.selectedPayment.collectAsState()
    var isAgreementChecked by remember { mutableStateOf(false) }
    val selectedAddress by paymentViewModel.selectedAddress.collectAsState()
    val selectedSize by paymentViewModel.selectedSize.collectAsState()

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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
            ) {
                BottomBarButton(
                    isKakaoPay = true,
                    text = "%,d원 결제하기".format(selectedPrice),
                    backgroundColor = if (isAgreementChecked && selectedOption != null) Color(0xFFFFEB00) else Color(0xFFE8E8E8),
                    textColor = if (isAgreementChecked && selectedOption != null) Color(0xFF000000) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
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
                .padding(horizontal = 16.dp, vertical = 2.dp),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            item {
                AddressSizeSelection(
                    navController = navController,
                    userViewModel = userViewModel,
                    paymentViewModel = paymentViewModel,
                    bottomSheetViewModel = bottomSheetViewModel
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
                PaymentAmount(amount = selectedPrice)
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            item {
                AgreementItem(
                    isRequired = true,
                    text = "개인정보 수집 및 이용 안내 동의",
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
