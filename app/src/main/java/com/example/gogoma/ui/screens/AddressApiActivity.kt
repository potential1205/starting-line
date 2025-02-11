package com.example.gogoma.ui.screens

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.gogoma.R

class AddressApiActivity : AppCompatActivity() {

    // JavaScript에서 호출할 인터페이스 클래스
    inner class MyJavaScriptInterface {
        @JavascriptInterface
        fun processDATA(data: String) {
            // 선택된 주소를 인텐트에 담아 결과로 전달
            val resultIntent = Intent()
            resultIntent.putExtra("data", data)
            setResult(Activity.RESULT_OK, resultIntent)
            finish() // 액티비티 종료
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address_api)

        val webView = findViewById<WebView>(R.id.webView)
        webView.settings.javaScriptEnabled = true
        // 로컬 파일에서 외부 리소스에 접근하도록 허용
        webView.settings.allowFileAccessFromFileURLs = true
        webView.settings.allowUniversalAccessFromFileURLs = true

        webView.addJavascriptInterface(MyJavaScriptInterface(), "Android")
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                // 페이지 로딩 완료 후 우편번호 API 실행 (HTML 내의 JavaScript 함수 호출)
                webView.loadUrl("javascript:sample2_execDaumPostcode();")
            }
        }
        webView.loadUrl("file:///android_asset/daum.html")
    }
}

