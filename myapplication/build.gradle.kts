plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.myapplication"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 33
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
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
    }
    composeOptions {
        // compose 컴파일러 확장 버전을 명시해 주세요.
        kotlinCompilerExtensionVersion = "1.4.0"
    }
}

dependencies {
    implementation(libs.play.services.wearable)
    implementation(platform(libs.androidx.compose.bom))

    // Compose UI 핵심 라이브러리
    implementation(libs.androidx.ui)          // 일반적으로 'androidx.compose.ui:ui'를 가리킵니다.
    implementation(libs.androidx.ui.graphics)   // 'androidx.compose.ui:ui-graphics'
    implementation(libs.androidx.compose.material) // Material Design 컴포넌트

    // 기타 UI 관련 라이브러리
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.wear.tooling.preview)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.splashscreen)

    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation("com.google.android.gms:play-services-wearable:18.2.0")
    // rest api
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    // 필요시 로깅 인터셉터 추가
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")

    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1") // 최신 버전 확인 필요

    implementation("androidx.appcompat:appcompat:1.6.1")
}
