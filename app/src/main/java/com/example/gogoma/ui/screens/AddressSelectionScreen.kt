package com.example.gogoma.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.gogoma.theme.GogomaTheme
import com.example.gogoma.ui.components.TopBarArrow
import com.example.gogoma.theme.BrandColor1
import com.example.gogoma.ui.components.AddressRadioItem
import com.example.gogoma.viewmodel.PaymentViewModel

@Composable
fun AddressSelectionScreen(
    navController: NavController,
    viewModel: PaymentViewModel = viewModel()
) {
    val addressList by viewModel.addressList.collectAsState()
    val selectedAddress by viewModel.selectedAddress.collectAsState()

    Scaffold(
        topBar = { TopBarArrow(title = "배송지", onBackClick = { navController.popBackStack() }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Button(
                onClick = { /* TODO: 신규 배송지 추가 */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = BrandColor1)
            ) {
                Text("+ 신규 배송지 추가")
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(addressList) { address ->
                    AddressRadioItem(
                        address = address,
                        isSelected = address == selectedAddress,
                        onSelect = {
                            viewModel.selectAddress(it)
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}
