plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.milkman.demo.model"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
        targetSdk = 33


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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    val libraries: Map<String, Any> by project

    //Room
    implementation(libraries["room_core"].toString())
    implementation(libraries["room_ktx"].toString())
    kapt(libraries["room_compiler"].toString())

    implementation(libraries["kotlin_core"].toString())

    implementation(libraries["app_compat"].toString())
    implementation(libraries["material"].toString())

    testImplementation(libraries["junit"].toString())
    androidTestImplementation(libraries["test_junit"].toString())
    androidTestImplementation(libraries["espresso_core"].toString())
}