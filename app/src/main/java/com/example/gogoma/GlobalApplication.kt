package com.example.gogoma

import android.app.Application
import android.util.Log
import com.example.gogoma.data.model.MarathonStartInitDataResponse
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {
    var initData: MarathonStartInitDataResponse? = null
    override fun onCreate() {
        super.onCreate()
        // 카카오 SDK 초기화
        KakaoSdk.init(this, BuildConfig.KAKAO_APP_KEY)
    }
}