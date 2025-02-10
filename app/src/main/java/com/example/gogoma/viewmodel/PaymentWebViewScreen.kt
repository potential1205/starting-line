package com.example.gogoma.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.gogoma.viewmodel.PaymentViewModel


@SuppressLint("SetJavaScriptEnabled")
@Composable
fun PaymentWebViewScreen(
    navController: NavController,
    paymentUrl: String,
    viewModel: PaymentViewModel,
    registJson: String
) {
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    mixedContentMode = android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                }

                webChromeClient = WebChromeClient()

                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                        val url = request?.url.toString()
                        Log.d("PaymentWebViewScreen", "🔗 로딩 중인 URL: $url")

                        return when {
                            url.startsWith("intent://") -> {
                                handleIntentScheme(context, view, url)
                            }

                            url.contains("gogoma://payment/result/success") -> {
                                val pgToken = Uri.parse(url).getQueryParameter("pg_token")
                                if (!pgToken.isNullOrEmpty()) {
                                    val redirectUrl = "gogoma://payment/result/success"
                                    viewModel.redirectAfterPayment(pgToken, redirectUrl) { isSuccess ->
                                        if (isSuccess) {
                                            navController.navigate("paymentSuccess/${Uri.encode(registJson)}")
                                        } else {
                                            navController.navigate("paymentFailure")
                                        }
                                    }
                                } else {
                                    Log.e("PaymentWebViewScreen", "❌ pg_token이 URL에 포함되어 있지 않습니다.")
                                    navController.navigate("paymentFailure")
                                }
                                true
                            }

                            url.contains("gogoma://payment/result/fail") || url.contains("gogoma://payment/result/cancel") -> {
                                navController.navigate("paymentFailure")
                                true
                            }

                            else -> false
                        }
                    }
                }

                loadUrl(paymentUrl)
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}

private fun handleIntentScheme(context: Context, view: WebView?, url: String): Boolean {
    return try {
        val intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
        try {
            context.startActivity(intent)
            true
        } catch (e: Exception) {
            Log.e("PaymentWebViewScreen", "❌ 인텐트 실행 실패: ${e.message}")
            val fallbackUrl = intent.getStringExtra("browser_fallback_url")
            if (!fallbackUrl.isNullOrEmpty()) {
                Log.d("PaymentWebViewScreen", "🌐 Fallback URL 로드: $fallbackUrl")
                view?.loadUrl(fallbackUrl)
            }
            true
        }
    } catch (e: Exception) {
        Log.e("PaymentWebViewScreen", "❌ 인텐트 파싱 실패: ${e.message}")
        true
    }
}

fun isAppInstalled(context: Context, packageName: String): Boolean {
    return try {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            context.packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
        } else {
            @Suppress("DEPRECATION")
            context.packageManager.getPackageInfo(packageName, 0)
        }
        Log.d("PaymentWebViewScreen", "✅ $packageName 설치됨")
        true
    } catch (e: PackageManager.NameNotFoundException) {
        Log.e("PaymentWebViewScreen", "❌ $packageName 설치되지 않음")
        false
    }
}


