import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-parcelize")
    kotlin("kapt")
}

android {
    namespace = "com.example.gogoma"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.gogoma"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Kakao API Key를 BuildConfig에 추가
        val localProperties = rootProject.file("local.properties")
        val properties = Properties()
        properties.load(localProperties.inputStream())
        val kakaoApiKey = properties.getProperty("KAKAO_APP_KEY")

        // BuildConfig에 키 추가
        buildConfigField("String", "KAKAO_APP_KEY", "\"${kakaoApiKey}\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    // Retrofit & OkHttp
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    // Coroutine
    implementation(libs.coroutines)
    // Security (EncryptedSharedPreferences)
    implementation(libs.security.crypto)

    implementation(libs.androidx.browser)

    implementation(libs.coil.compose)

    implementation(libs.gson)

    implementation(libs.v2.user) // 카카오 로그인 API 모듈

    // Room 관련 의존성
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler)

}