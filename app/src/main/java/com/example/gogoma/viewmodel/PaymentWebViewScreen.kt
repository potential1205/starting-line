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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.gogoma.data.dto.UserMarathonSearchDto
import com.example.gogoma.viewmodel.PaymentViewModel
import com.google.gson.Gson
import org.json.JSONObject


@SuppressLint("SetJavaScriptEnabled")
@Composable
fun PaymentWebViewScreen(
    navController: NavController,
    paymentUrl: String,
    viewModel: PaymentViewModel,
    registJson: String
) {
    val context = LocalContext.current
    val gson = remember { Gson() }

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
                                    viewModel.redirectAfterPayment(pgToken, redirectUrl, context) { isSuccess ->
                                        if (isSuccess) {
                                            val regist = viewModel.getRegistFromJson(registJson)
                                            if (regist != null) {
                                                val dto = gson.fromJson(registJson, UserMarathonSearchDto::class.java)
                                                val marathonTitle = dto.marathonTitle ?: "마라톤 제목 없음"

                                                val jsonObject = JSONObject(registJson)
                                                jsonObject.put("marathonTitle", marathonTitle)
                                                val enrichedJson = jsonObject.toString()
                                                Log.d("PaymentWebViewScreen", "🟦 marathonTitle 동적 추가된 JSON: $enrichedJson")
                                                viewModel.checkAndRegisterMarathon(
                                                    regist,
                                                    context
                                                ) { registered ->
                                                    if (registered) {
                                                        val encodedJson = Uri.encode(enrichedJson)
                                                        Log.d(
                                                            "PaymentWebViewScreen",
                                                            "✅ 등록 및 성공 화면 이동 완료"
                                                        )
                                                        navController.navigate("paymentSuccess/$encodedJson")
                                                    } else {
                                                        Log.e("PaymentWebViewScreen", "❌ 마라톤 등록 실패")
                                                        navController.navigate("paymentFailure")
                                                    }
                                                }
                                            } else {
                                                Log.e("PaymentWebViewScreen", "❌ regist 생성 실패")
                                                navController.navigate("paymentFailure")
                                            }
                                        } else {
                                            Log.e("PaymentWebViewScreen", "❌ 리다이렉트 실패")
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

//fun isAppInstalled(context: Context, packageName: String): Boolean {
//    return try {
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
//            context.packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
//        } else {
//            @Suppress("DEPRECATION")
//            context.packageManager.getPackageInfo(packageName, 0)
//        }
//        Log.d("PaymentWebViewScreen", "✅ $packageName 설치됨")
//        true
//    } catch (e: PackageManager.NameNotFoundException) {
//        Log.e("PaymentWebViewScreen", "❌ $packageName 설치되지 않음")
//        false
//    }
//}


