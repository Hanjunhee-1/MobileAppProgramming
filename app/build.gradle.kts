plugins {
    alias(libs.plugins.android.application)

    // Google Service
    id("com.google.gms.google-services")
}

android {
    namespace = "edu.skku.cs.personalproject"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "edu.skku.cs.personalproject"
        minSdk = 29
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        buildFeatures {
            viewBinding = true
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    packaging {

        resources {

            excludes += "META-INF/INDEX.LIST"

            excludes += "META-INF/DEPENDENCIES"

            excludes += "META-INF/LICENSE"

            excludes += "META-INF/LICENSE.txt"

            excludes += "META-INF/NOTICE"

            excludes += "META-INF/NOTICE.txt"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    // Coroutine
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.4")

    // Material UI
    implementation("com.google.android.material:material:1.12.0")

    // RecyclerView
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // Chart
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:34.13.0"))

    // Firebase Authentication (Firebase 내장 구글 로그인을 지원하는 인증 라이브러리)
    implementation("com.google.firebase:firebase-auth")

    // Google Play Services Auth (구글 로그인 버튼 클릭 시 계정 선택 창을 띄워주는 최신 라이브러리)
    implementation("com.google.android.gms:play-services-auth:21.2.0")
}