package com.example.gogoma.services

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.example.gogoma.data.repository.UserDistanceRepository
import com.google.android.gms.location.*

class LocationForegroundService : Service() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    // 누적 거리 (킬로미터 단위)
    private var cumulativeDistance: Int = 0
    private var previousLocation: Location? = null

    // 예시 사용자 ID (백엔드에서 auto PK로 생성된 ID; 여기서는 "1" 사용)
    private val userId = 0

    override fun onCreate() {
        super.onCreate()
        // FusedLocationProviderClient 초기화
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // 5초마다 위치 업데이트 요청
        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000L)
            .setMinUpdateIntervalMillis(5000)
            .build()

        // 위치 업데이트 콜백 설정
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (currentLocation in locationResult.locations) {
                    Log.d("LocationForegroundService", "현재 위치 -> 위도: ${currentLocation.latitude}, 경도: ${currentLocation.longitude}")

                    // 이전 위치가 존재하면, 두 위치 사이의 거리를 계산하여 누적 거리 업데이트
                    previousLocation?.let { prevLoc ->
                        val distanceInMeters = Math.round(prevLoc.distanceTo(currentLocation))
                        val incrementalDistance = distanceInMeters
                        cumulativeDistance += incrementalDistance

                        val formattedCumulative = formatDistance(cumulativeDistance)
                        val formattedIncrement = formatDistance(incrementalDistance)
                        Log.d("MarathonDistanceGPSActivity", "이번 이동 거리: $formattedIncrement km, 누적 거리: $formattedCumulative km")

                        // Firebase에 누적 거리 업데이트
                        UserDistanceRepository.updateUserCumulativeDistance(
                            userId,
                            cumulativeDistance,
                            onSuccess = {
                                Log.d("LocationForegroundService", "Firebase 업데이트 성공: $cumulativeDistance km")
                            },
                            onFailure = { exception ->
                                Log.e("LocationForegroundService", "Firebase 업데이트 실패: ${exception.message}")
                            }
                        )
                    }

                    if (previousLocation == null) {
                        Log.d("LocationForegroundService", "첫 위치 업데이트입니다.")
                    }

                    // 현재 위치를 이전 위치로 저장
                    previousLocation = currentLocation
                }
            }
        }

        // Foreground Service 시작 및 위치 업데이트 시작
        startForegroundServiceWithNotification()
        startLocationUpdates()
    }

    private fun formatDistance(distance: Int): String {
        return String.format("%.2f", distance / 100000.0)
    }

    private fun startForegroundServiceWithNotification() {
        val channelId = "LocationServiceChannel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Location Service",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
        val notification: Notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("위치 추적 중")
            .setContentText("앱이 백그라운드에서 위치를 추적합니다.")
            .setSmallIcon(android.R.drawable.ic_menu_mylocation)
            .build()
        // startForeground() 호출로 서비스가 포그라운드 서비스로 실행됨
        startForeground(1, notification)
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("LocationForegroundService", "위치 권한 부족")
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
