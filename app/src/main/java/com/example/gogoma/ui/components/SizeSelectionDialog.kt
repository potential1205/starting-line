package com.example.gogoma.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gogoma.viewmodel.PaymentViewModel
import com.example.gogoma.theme.BrandColor1

@Composable
fun SizeSelectionDialog(
    viewModel: PaymentViewModel,
    onDismiss: () -> Unit
) {
    val sizeOptions = listOf("85", "90", "95", "100", "105")
    var selectedSize by remember { mutableStateOf(viewModel.selectedSize.value) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "옷 사이즈 선택",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                sizeOptions.forEach { size ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedSize = size }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (size == selectedSize),
                            onClick = { selectedSize = size }
                        )
                        Text(
                            text = size,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        viewModel.updateSelectedSize(selectedSize)
                        onDismiss() // 모달 닫기
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BrandColor1),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("확인")
                }
            }
        }
    }
}
