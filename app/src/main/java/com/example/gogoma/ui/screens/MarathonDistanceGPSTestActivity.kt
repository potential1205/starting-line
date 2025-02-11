package com.example.gogoma.ui.screens// 파일 위치: app/src/main/java/com/example/testapplication/ui/screens/MarathonDistanceGPSActivity.kt

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gogoma.services.LocationForegroundService

class MarathonDistanceGPSTestActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Foreground Service를 시작하여 백그라운드에서 5초마다 위치 업데이트 및 DB 업데이트 로직을 처리합니다.
        val serviceIntent = Intent(this, LocationForegroundService::class.java)
        startForegroundService(serviceIntent)

        // 간단한 UI를 표시합니다.
        setContent {
            MarathonDistanceGPSScreen()
        }
    }
}

@Composable
fun MarathonDistanceGPSScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "위치 업데이트 서비스가 실행 중입니다.")
        Text(text = "백그라운드에서 위치 업데이트 및 DB 업데이트를 수행 중입니다. 로그를 확인하세요.")
    }
}
