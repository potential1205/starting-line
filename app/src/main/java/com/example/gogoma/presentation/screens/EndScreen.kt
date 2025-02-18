package com.example.gogoma.presentation.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.tooling.preview.devices.WearDevices
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable

@Composable
fun EndScreen() {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = { sendEndSignalToPhone(context) },
            modifier = Modifier.size(200.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
            shape = CircleShape
        ) {
            Text("종료", fontSize = 40.sp, color = MaterialTheme.colors.onPrimary)
        }
    }
}

// 📌 워치에서 모바일로 End 신호 전송
private fun sendEndSignalToPhone(context: Context) {
    val putDataMapRequest = PutDataMapRequest.create("/end").apply {
        dataMap.putLong("timestamp", System.currentTimeMillis())
        dataMap.putString("priority", "urgent")
    }

    val putDataRequest = putDataMapRequest.asPutDataRequest().setUrgent()

    Wearable.getDataClient(context).putDataItem(putDataRequest)
        .addOnSuccessListener {
            Log.d("EndScreen", "[워치 to 모바일] 마라톤 종료 요청 성공")
        }
        .addOnFailureListener { e ->
            Log.e("EndScreen", "[워치 to 모바일] 마라톤 종료 요청 실패", e)
        }
}

@Preview(device = WearDevices.SMALL_ROUND, showSystemUi = true)
@Composable
fun EndScreenPreview() {
    EndScreen()
}