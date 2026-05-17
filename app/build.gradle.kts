plugins {
    alias(libs.plugins.android.application)
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
}