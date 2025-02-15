package com.example.gogoma.data.manager

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.util.Log
import com.example.gogoma.data.repository.UserDistanceRepository
import com.google.android.gms.location.*

/**
 * MarathonStartManager는 마라톤 시작 시 필요한 초기 데이터 생성 및 위치 업데이트 시작을 담당합니다.
 */
class MarathonStartManager(
    private val context: Context,
    private val currentUserId: Int,
    // UI 업데이트를 위한 콜백: 누적거리, 이번 이동 거리(둘 다 km 단위 문자열)
    private val onUpdateUI: (formattedCumulative: String, formattedIncrement: String) -> Unit,
    // 위치 업데이트가 있을 때 추가 처리가 필요하면 사용할 콜백 (옵션)
    private val onLocationUpdate: ((Location) -> Unit)? = null
) {
    private val TAG = "MarathonStartManager"
    private var cumulativeDistance: Int = 0
    private var previousLocation: Location? = null

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    init {
        setupLocationRequest()
    }

    private fun setupLocationRequest() {
        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000L)
            .setMinUpdateIntervalMillis(5000)
            .build()
    }

    /**
     * startMarathonRun() 초기 데이터 설정(누적 거리 0으로 DB 업데이트) 후 위치 업데이트를 시작합니다.
     */
    @SuppressLint("MissingPermission")
    fun startMarathonRun() {
        // 초기 데이터: 누적 거리를 0으로 설정하고 DB에 업데이트
        cumulativeDistance = 0
        previousLocation = null

        UserDistanceRepository.updateUserCumulativeDistance(
            currentUserId,
            cumulativeDistance,
            onSuccess = { Log.d(TAG, "초기 데이터 생성 성공: 0 cm") },
            onFailure = { exception ->
                Log.e(TAG, "초기 데이터 생성 실패: ${exception.message}")
            }
        )

        startLocationUpdates()
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "위치 권한 부족")
            return
        }

        locationCallback = object : LocationCallback() {
            @SuppressLint("DefaultLocale")
            override fun onLocationResult(locationResult: LocationResult) {
                for (currentLocation in locationResult.locations) {
                    Log.d(TAG, "현재 위치 -> 위도: ${currentLocation.latitude}, 경도: ${currentLocation.longitude}")

                    previousLocation?.let { prevLoc ->
                        // 이전 위치와 현재 위치 간 거리(cm) 계산
                        val distanceInCm = Math.round(prevLoc.distanceTo(currentLocation) * 100)
                        cumulativeDistance += distanceInCm

                        // 누적 거리와 이번 이동 거리를 km 단위(소수 둘째자리 문자열)로 포맷
                        val formattedCumulative = String.format("%.2f", cumulativeDistance / 100000.0)
                        val formattedIncrement = String.format("%.2f", distanceInCm / 100000.0)
                        Log.d(TAG, "이번 이동: $formattedIncrement km, 누적: $formattedCumulative km")

                        onUpdateUI(formattedCumulative, formattedIncrement)

                        // DB 업데이트
                        UserDistanceRepository.updateUserCumulativeDistance(
                            currentUserId,
                            cumulativeDistance,
                            onSuccess = { Log.d(TAG, "DB 업데이트 성공: ${cumulativeDistance} cm") },
                            onFailure = { exception ->
                                Log.e(TAG, "DB 업데이트 실패: ${exception.message}")
                            }
                        )
                    }
                    if (previousLocation == null) {
                        Log.d(TAG, "첫 위치 업데이트입니다.")
                    }
                    previousLocation = currentLocation
                    onLocationUpdate?.invoke(currentLocation)
                }
            }
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}

/*

// ------------------------------ [Marathon Start] ------------------------------
    @SuppressLint("VisibleForTests")
    private fun startMarathonRun() {
        // 1. 초기 데이터 생성: 누적 거리를 0 (cm 단위)로 DB에 저장
        UserDistanceRepository.createInitialUserData(
            currentUserId,
            onSuccess = { Log.d("MarathonRunService", "초기 데이터 생성 성공") },
            onFailure = { exception ->
                Log.e("MarathonRunService", "초기 데이터 생성 실패: ${exception.message}")
            }
        )
        cumulativeDistance = 0
        displayedCumulative.value = "0.00"
        displayedIncremental.value = "0.00"
        previousLocation = null  // 초기화

        // 2. 위치 업데이트 시작: fusedLocationClient 초기화
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000L)
            .setMinUpdateIntervalMillis(5000)
            .build()

        locationCallback = object : LocationCallback() {
            @SuppressLint("DefaultLocale")
            override fun onLocationResult(locationResult: LocationResult) {
                for (currentLocation in locationResult.locations) {
                    Log.d("MarathonRunService", "현재 위치 -> 위도: ${currentLocation.latitude}, 경도: ${currentLocation.longitude}")
                    previousLocation?.let { prevLoc ->
                        // 이전 위치와 현재 위치 간 거리를 구한 후 cm 단위로 변환
                        val distanceInCm = Math.round(prevLoc.distanceTo(currentLocation) * 100)
                        cumulativeDistance += distanceInCm

                        // 화면 표시용: 누적 거리와 이번 이동 거리를 km 단위로 변환 (1km = 100000cm) 후 소수 둘째자리로 포맷
                        val formattedCumulative = String.format("%.2f", cumulativeDistance / 100000.0)
                        val formattedIncrement = String.format("%.2f", distanceInCm / 100000.0)
                        Log.d("MarathonRunService", "이번 이동 거리: $formattedIncrement km, 누적 거리: $formattedCumulative km")

                        // UI 상태 업데이트
                        runOnUiThread {
                            displayedCumulative.value = formattedCumulative
                            displayedIncremental.value = formattedIncrement
                        }

                        // DB 업데이트: 누적 거리를 cm 단위로 업데이트
                        UserDistanceRepository.updateUserCumulativeDistance(
                            currentUserId,
                            cumulativeDistance,
                            onSuccess = {
                                Log.d("MarathonRunService", "Firebase 업데이트 성공: ${cumulativeDistance} cm")
                            },
                            onFailure = { exception ->
                                Log.e("MarathonRunService", "Firebase 업데이트 실패: ${exception.message}")
                            }
                        )
                    }
                    if (previousLocation == null) {
                        Log.d("MarathonRunService", "첫 위치 업데이트입니다.")
                    }
                    previousLocation = currentLocation
                }
            }
        }

        startLocationUpdates()
    }

    private fun startLocationUpdates() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("MarathonRunService", "위치 권한 부족")
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
 */
