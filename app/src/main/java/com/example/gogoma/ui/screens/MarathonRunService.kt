package com.example.gogoma.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.gogoma.data.api.RetrofitInstance
import com.example.gogoma.data.dto.MarathonReadyDto
import com.example.gogoma.data.dto.MyData
import com.example.gogoma.data.repository.UserDistanceRepository
import com.google.android.gms.location.*
import com.google.android.gms.wearable.*
import kotlinx.coroutines.delay
import retrofit2.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MarathonRunService : ComponentActivity(), DataClient.OnDataChangedListener {
    private var receivedState = mutableStateOf("Waiting for data...")
    private var marathonReadyData = mutableStateOf<MarathonReadyDto?>(null)
    private val isAutoSending = mutableStateOf(false)
    private val isMarathonReady = mutableStateOf(false)

    // 계산 관련 (누적 거리를 cm 단위로 저장)
    private var cumulativeDistance: Int = 0
    private var previousLocation: Location? = null

    // 예시 사용자 ID
    private val userId = "1"

    // UI 상태: 화면에는 누적 거리와 최근 이동 거리를 km 단위(소수 둘째자리)로 표시
    private val displayedCumulative = mutableStateOf("0.00")
    private val displayedIncremental = mutableStateOf("0.00")

    // Location 관련 변수
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    // ** ActivityResultLauncher는 onCreate에서 미리 등록 **
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ActivityResultLauncher를 onCreate에서 미리 등록해야 함
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true &&
                    permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
                    startLocationUpdates()
                } else {
                    Log.e("MarathonRunService", "위치 권한이 거부되었습니다.")
                }
            }

        Wearable.getDataClient(this).addListener(this)

        setContent {
            PhoneScreen(
                marathonReadyData = marathonReadyData.value,
                isAutoSending = isAutoSending,
                onMarathonReady = { marathonReady() },
                onSendData = { sendDataToWearable(MyData("김용현", 100)) },
                isMarathonReady = isMarathonReady,
                onStart = { startMarathonRun() },
                displayedCumulative = displayedCumulative.value,
                displayedIncremental = displayedIncremental.value
            )
        }
    }

    // --------------- [초기 데이터 불러오기 / from 서버] ---------------
    //@SuppressLint("VisibleForTests")
    private fun marathonReady() {
        Log.e("MarathonRunService", "marathonReady 호출")

        val userId = 5
        val marathonId = 25
        val token = "kakao access token" // 만약 Authorization 헤더가 필요하면, 인터페이스에서 주석 해제 후 사용

        // RetrofitInstance를 사용하여 API 호출
        RetrofitInstance.marathonRunApiService.startMarathon(marathonId, userId)
            .enqueue(object : Callback<MarathonReadyDto> {
                override fun onResponse(
                    call: Call<MarathonReadyDto>,
                    response: Response<MarathonReadyDto>
                ) {
                    if (response.isSuccessful) {
                        Log.d("MarathonRunService", "Marathon Ready 성공: ${response.body()}")
                        marathonReadyData.value = response.body()
                        isMarathonReady.value = true
                        // 필요시 sendMarathonReady() 호출
                    } else {
                        Log.e("MarathonRunService", "Marathon Ready 응답 에러 ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<MarathonReadyDto>, t: Throwable) {
                    val errorMessage = t.localizedMessage ?: "알 수 없는 에러"
                    Log.e("MarathonRunService", "Marathon Ready 호출 실패: $errorMessage\n${Log.getStackTraceString(t)}")
                }
            })
    }
    // --------------- [Start 버튼 클릭 시 실행되는 로직] ---------------
    @SuppressLint("VisibleForTests")
    private fun startMarathonRun() {
        // 1. 초기 데이터 생성: 누적 거리를 0 (cm 단위)로 DB에 저장
        UserDistanceRepository.createInitialUserData(
            userId,
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
                            userId,
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
        requestLocationPermissions()
    }

    // ActivityResultLauncher를 onCreate에서 등록했으므로 여기서는 launch만 호출
    private fun requestLocationPermissions() {
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
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

    // --------------- [데이터 송수신 이벤트 감지 / from 워치] ---------------
    @SuppressLint("VisibleForTests")
    override fun onDataChanged(dataEvents: DataEventBuffer) {
        for (event in dataEvents) {
            if (event.type == DataEvent.TYPE_CHANGED) {
                val dataItem = event.dataItem

                when (dataItem.uri.path) {
                    "/start" -> { // 워치로부터 /start 요청이 오면
                        val dataMapItem = DataMapItem.fromDataItem(dataItem)
                        val timestamp = dataMapItem.dataMap.getLong("time")
                        isAutoSending.value = true
                        runOnUiThread {
                            receivedState.value = "start time : $timestamp"
                        }
                        Log.d("MarathonRunService", "[mobile -> watch] Marathon Start 성공")
                    }
                    "/end" -> { // 워치로부터 /end 요청이 오면
                        isAutoSending.value = false
                        runOnUiThread {
                            Log.d("MarathonRunService", "Marathon End 성공")
                        }
                    }
                }
            }
        }
    }

    // --------------- [데이터 송신 / to 워치] ---------------
    @SuppressLint("VisibleForTests")
    private fun sendDataToWearable(data: MyData) {
        val putDataMapRequest = PutDataMapRequest.create("/update").apply {
            dataMap.putInt("age", data.age)
            dataMap.putString("name", data.name)
            dataMap.putLong("timestamp", System.currentTimeMillis())
        }

        val putDataRequest = putDataMapRequest.asPutDataRequest().setUrgent()

        Wearable.getDataClient(this).putDataItem(putDataRequest)
            .addOnSuccessListener { dataItem ->
                Log.e("MarathonRunService", "[mobile -> watch] 데이터 전송 성공: $dataItem")
            }
            .addOnFailureListener { e ->
                Log.e("MarathonRunService", "[mobile -> watch] 데이터 전송 실패", e)
            }
    }

    @SuppressLint("VisibleForTests")
    private fun sendMarathonReady() {
        val putDataMapRequest = PutDataMapRequest.create("/ready").apply {
            dataMap.putLong("timestamp", System.currentTimeMillis())
        }

        val putDataRequest = putDataMapRequest.asPutDataRequest().setUrgent()

        Wearable.getDataClient(this).putDataItem(putDataRequest)
            .addOnSuccessListener { dataItem ->
                Log.e("MarathonRunService", "[mobile -> watch] Marathon Ready 전송 성공: $dataItem")
            }
            .addOnFailureListener { e ->
                Log.e("MarathonRunService", "[mobile -> watch] Marathon Ready 전송 실패", e)
            }
    }

    override fun onDestroy() {
        Wearable.getDataClient(this).removeListener(this)
        super.onDestroy()
    }
}

// --------------- [화면] ---------------
@Composable
fun PhoneScreen(
    onSendData: () -> Unit,
    onMarathonReady: () -> Unit,
    onStart: () -> Unit,
    marathonReadyData: MarathonReadyDto?,
    isAutoSending: MutableState<Boolean>,
    isMarathonReady: MutableState<Boolean>,
    displayedCumulative: String,
    displayedIncremental: String
) {
    LaunchedEffect(isAutoSending.value) {
        while (isAutoSending.value) {
            onSendData()
            delay(1000)
        }
    }

    val currentTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Time: $currentTime")
        Text(text = "Marathon Ready Data:\n$marathonReadyData")
        Text(text = "누적 거리: $displayedCumulative km")
        Text(text = "최근 이동 거리: $displayedIncremental km")
        // Ready 버튼: 서버에서 Ready 데이터를 가져옴
        if (!isMarathonReady.value) {
            Button(onClick = { onMarathonReady() }) {
                Text(text = "Ready")
            }
        }
        // Start 버튼: GPS 위치 업데이트 및 DB 초기화 실행
        Button(onClick = { onStart() }) {
            Text(text = "Start")
        }
    }
}
