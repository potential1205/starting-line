package com.example.gogoma.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.gogoma.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "새로운 토큰: $token")
        // 여기서 서버로 토큰을 전송하는 로직 추가 가능 (예: 사용자 정보 업데이트)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("FCM", "알림 수신: ${remoteMessage.data}")

        // FCM 메시지의 notification 필드가 있을 경우 처리
        remoteMessage.notification?.let {
            showNotification(it.title, it.body)
        }

        // 또는 데이터 메시지로 온 경우 직접 데이터를 활용하여 알림 처리 가능
    }

    private fun showNotification(title: String?, message: String?) {
        val channelId = "my_channel"
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.logo_image) // 앱에 맞는 아이콘 리소스로 변경
            .setContentTitle(title ?: "알림")
            .setContentText(message ?: "메시지 내용")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 안드로이드 8.0 이상에서는 알림 채널 생성 필요
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "대회 알림",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "대회 신청 관련 알림"
            }
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notificationBuilder.build())
    }
}
