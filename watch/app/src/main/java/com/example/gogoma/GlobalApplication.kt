package com.example.gogoma

import android.app.Application
import android.util.Log
import androidx.navigation.NavController
import com.example.gogoma.presentation.services.MarathonEventManager
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.Wearable

class GlobalApplication : Application(), DataClient.OnDataChangedListener {

    var navController: NavController? = null

    override fun onCreate() {
        super.onCreate()
        Wearable.getDataClient(this).addListener(this)
        Log.d("GlobalApplication", "Data Layer 이벤트 리스너 전역 등록 완료!")
    }

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        navController?.let { MarathonEventManager.handleDataChanged(dataEvents, it) }
    }
}