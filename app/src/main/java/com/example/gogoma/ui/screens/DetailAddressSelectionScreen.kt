package com.example.gogoma.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.gogoma.ui.components.TopBarArrow
import com.example.gogoma.viewmodel.PaymentViewModel

@Composable
fun DetailAddressSelectionScreen (
    navController: NavController,
    paymentViewModel: PaymentViewModel
) {
    val address = paymentViewModel.tmpAddress.collectAsState()
    var detailAddress by remember { mutableStateOf("") }


    Scaffold(
        topBar = { TopBarArrow(title = "주소 설정", onBackClick = { navController.popBackStack() }) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ){
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 15.dp, horizontal = 20.dp)
            ){
                address.value?.let { address ->
                    Text(
                        text = address,
                        fontWeight = FontWeight.Bold
                    )
                    OutlinedTextField(
                        value = detailAddress,
                        onValueChange = { detailAddress = it },
                        label = { Text("상세 주소") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(
                        onClick = {
                            paymentViewModel.updateAddress(address, detailAddress)
                            navController.popBackStack()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("주소 설정")
                    }
                } ?: run {
                    Text(text = "주소가 잘못 선택되었습니다.")
                    Button(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Text("이전 화면으로")
                    }
                }
            }

        }
    }
}

@Preview
@Composable
fun DetailAddressSelectionScreenPreview() {
    DetailAddressSelectionScreen(rememberNavController(), PaymentViewModel())
}