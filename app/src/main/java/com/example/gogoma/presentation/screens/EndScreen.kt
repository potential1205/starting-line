package com.example.gogoma.presentation.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.tooling.preview.devices.WearDevices
import com.example.gogoma.presentation.theme.GogomaWatchTheme
import com.example.gogoma.presentation.viewmodel.MarathonDataViewModel
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import kotlin.math.floor

@Composable
fun EndScreen(marathonDataViewModel: MarathonDataViewModel, context: Context) {

    val marathonState = marathonDataViewModel.marathonState.collectAsState().value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxHeight()
        ) {
            // 등수 및 참가자 수 표시 (상단으로 이동)
            Box(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Text(
                    text = "${marathonState.myRank} / ${marathonState.totalMemberCount} 등",
                    style = MaterialTheme.typography.title2,
                    textAlign = TextAlign.Center
                )
            }

            // 중간 정보 표시
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "평균 페이스: ${formatPace(marathonState.currentPace)} /km",
                    style = MaterialTheme.typography.body2,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "달린 시간: ${formatTime(marathonState.currentTime)}",
                    style = MaterialTheme.typography.body2,
                    textAlign = TextAlign.Center
                )
            }

            // 종료 버튼
            Box(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Button(
                    onClick = { sendEndSignalToPhone(context) },
                    modifier = Modifier.size(50.dp)
                ) {
                    Text("종료", style = MaterialTheme.typography.button, color = MaterialTheme.colors.onPrimary)
                }
            }
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
    GogomaWatchTheme {
        EndScreen(marathonDataViewModel = MarathonDataViewModel(), context = android.content.ContextWrapper(null))
    }
}