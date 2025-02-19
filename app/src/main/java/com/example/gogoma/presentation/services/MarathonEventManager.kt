    package com.example.gogoma.presentation.services

    import android.content.Context
    import android.util.Log
    import androidx.compose.runtime.mutableStateOf
    import androidx.navigation.NavController
    import com.example.gogoma.presentation.data.MarathonRealTimeData
    import com.google.android.gms.wearable.DataClient
    import com.google.android.gms.wearable.DataEvent
    import com.google.android.gms.wearable.DataEventBuffer
    import com.google.android.gms.wearable.DataMapItem
    import com.google.android.gms.wearable.PutDataMapRequest
    import com.google.android.gms.wearable.Wearable
    import com.google.gson.Gson

    class MarathonEventManager(context: Context) {
        private val gson = Gson()
//        private val dataClient = Wearable.getDataClient(context)
        private lateinit var dataClient: DataClient

        // 상태 관리
        val isMarathonReady = mutableStateOf(false)

        fun init(context: Context) {
            dataClient = Wearable.getDataClient(context)
        }

        // [워치 -> 모바일] Start 신호 전송
        fun sendStartSignal() {
            val putDataMapRequest = PutDataMapRequest.create("/start").apply {
                dataMap.putLong("timestamp", System.currentTimeMillis())
                dataMap.putString("priority", "urgent")
            }

            val putDataRequest = putDataMapRequest.asPutDataRequest().setUrgent()

            dataClient.putDataItem(putDataRequest)
                .addOnSuccessListener { dataItem ->
                    Log.d("WatchEventManager", "Start 이벤트 전송 성공: $dataItem")
                }
                .addOnFailureListener { e ->
                    Log.e("WatchEventManager", "Start 이벤트 전송 실패", e)
                }
        }

        // [모바일 -> 워치] 데이터 이벤트 감지
        fun handleDataChanged(dataEvents: DataEventBuffer) {
            for (event in dataEvents) {
                if (event.type == DataEvent.TYPE_CHANGED) {
                    val path = event.dataItem.uri.path
                    Log.d("WatchEventManager", "데이터 변경 감지: $path")

                    when (path) {
                        "/ready" -> {
                            val dataMap = DataMapItem.fromDataItem(event.dataItem).dataMap
                            val marathonDataJson = dataMap.getString("marathonData")
                            val marathonData =
                                gson.fromJson(marathonDataJson, MarathonRealTimeData::class.java)
                            Log.d("WatchEventManager", "마라톤 준비 데이터 수신: $marathonData")
                            isMarathonReady.value = true
                        }

                        "/update" -> {
                            val dataMap = DataMapItem.fromDataItem(event.dataItem).dataMap
                            val marathonDataJson = dataMap.getString("marathonData")
                            val marathonData =
                                gson.fromJson(marathonDataJson, MarathonRealTimeData::class.java)
                            Log.d("WatchEventManager", "마라톤 업데이트 데이터 수신: $marathonData")
                        }
                    }
                }
            }
        }

        companion object {
            // 전역에서 호출할 수 있는 static 메서드
            fun handleDataChanged(dataEvents: DataEventBuffer, navController: NavController) {
                val gson = Gson()
                for (event in dataEvents) {
                    if (event.type == DataEvent.TYPE_CHANGED) {
                        val path = event.dataItem.uri.path
                        Log.d("GlobalWatchEventManager", "데이터 변경 감지: $path")

                        when (path) {
                            "/ready" -> {
                                val dataMap = DataMapItem.fromDataItem(event.dataItem).dataMap
                                val marathonDataJson = dataMap.getString("marathonData")
                                val marathonData = gson.fromJson(marathonDataJson, MarathonRealTimeData::class.java)
                                Log.d("GlobalWatchEventManager", "마라톤 준비 데이터 수신: $marathonData")
                            }
                            "/update" -> {
                                val dataMap = DataMapItem.fromDataItem(event.dataItem).dataMap
                                val marathonDataJson = dataMap.getString("marathonData")
                                val marathonData = gson.fromJson(marathonDataJson, MarathonRealTimeData::class.java)
                                Log.d("GlobalWatchEventManager", "마라톤 업데이트 데이터 수신: $marathonData")
                            }
                            "/end" -> {
                                Log.d("WatchEventManager", "End 이벤트 수신, 종료 화면으로 이동")
                                navController.navigate("endScreen")
                            }
                        }
                    }
                }
            }
        }
    }