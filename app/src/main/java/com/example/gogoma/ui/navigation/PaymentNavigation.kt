package com.example.gogoma.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gogoma.ui.screens.PaymentScreen
import com.example.gogoma.ui.screens.AddressSelectionScreen
import com.example.gogoma.viewmodel.PaymentViewModel

@Composable
fun PaymentNavigation() {
    val navController = rememberNavController()
    val viewModel: PaymentViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "payment"
    ) {
        // 결제 화면
        composable("payment") {
            PaymentScreen(navController = navController, viewModel = viewModel)
        }

        // 주소 선택 화면
        composable("addressSelection") {
            AddressSelectionScreen(navController = navController, viewModel = viewModel)
        }
    }
}
