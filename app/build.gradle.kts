plugins {
    alias(libs.plugins.android.application)
//    alias(libs.plugins.kotlin.android)
    id("com.google.devtools.ksp")
    kotlin("android") version "2.0.21"
}

android {
    namespace = "com.example.musicalquiz"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.musicalquiz"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx.v190)
    implementation(libs.androidx.appcompat)
    implementation(libs.material.v190)
    implementation(libs.androidx.constraintlayout)
    //Retrofit for API calls
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    //ViewModel and LiveData
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    // RecyclerView
    implementation(libs.androidx.recyclerview)
    // Room Database
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.databinding.adapters)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    //Glide for Image Loading
    implementation(libs.glide)
    annotationProcessor(libs.compiler)
    //Coroutines
    implementation(libs.kotlinx.coroutines.android)
    //viewModels
    implementation(libs.androidx.fragment.ktx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit.v121)
    androidTestImplementation(libs.androidx.espresso.core.v361)
}