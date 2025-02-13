import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-parcelize")
    id("com.google.devtools.ksp") version "2.0.0-1.0.24"

    id("com.google.gms.google-services") version "4.4.2" apply false
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

        // local.properties의 KEY들을 BuildConfig에 추가
        val localProperties = rootProject.file("local.properties")
        val properties = Properties()
        properties.load(localProperties.inputStream())

        val kakaoApiKey = properties.getProperty("KAKAO_APP_KEY")
        val clientID = properties.getProperty("CLIENT_ID")
        val redirectURI = properties.getProperty("REDIRECT_URI")

        // BuildConfig에 키 추가
        buildConfigField("String", "KAKAO_APP_KEY", "\"${kakaoApiKey}\"")
        buildConfigField("String", "CLIENT_ID", "\"${clientID}\"")
        buildConfigField("String", "REDIRECT_URI", "\"${redirectURI}\"")
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
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

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
    ksp(libs.androidx.room.compiler)

    // Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:33.8.0"))
    // Firebase Realtime Database 의존성 추가 (버전은 BoM에 의해 관리됨)
    implementation("com.google.firebase:firebase-database")
    // GPS 라이브러리
    implementation("com.google.android.gms:play-services-location:21.0.1")

    // 모바일 - 워치 통신 관련(data client)
    implementation("com.google.android.gms:play-services-wearable:18.0.0")
    implementation("androidx.appcompat:appcompat:1.6.1")

    // retrofit, restapi 관련
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    // gson
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")


}

apply(plugin = "com.google.gms.google-services")