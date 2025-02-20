package com.example.gogoma.ui.components.bottomsheets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gogoma.ui.components.FilterListItemContent
import com.example.gogoma.viewmodel.PaymentViewModel

@Composable
fun SizeSelectionBottomSheetContent (
    viewModel: PaymentViewModel,
    onDismiss: () -> Unit
) {
    val sizeOptions = listOf("85", "90", "95", "100", "105")
    var selectedSize by remember { mutableStateOf(viewModel.selectedSize.value) }

    BottomSheetContentWithTitle(
        title = "옷 사이즈",
        headerLeftContent = {
            Text(
                text = "닫기",
                fontSize = 14.5.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clickable { onDismiss() }
                    .padding(top = 1.dp)
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
        ) {
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    sizeOptions.forEach { size ->
                        FilterListItemContent(size, onClick = {
                            selectedSize = size
                            viewModel.updateSelectedSize(selectedSize)
                            onDismiss()
                        })
                    }
                }
            }
        }
    }
}