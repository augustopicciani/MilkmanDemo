plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id ("com.google.dagger.hilt.android")

}

android {
    namespace = "com.milkman.demo.data"
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

    implementation(project(":local"))
    implementation(project(":model"))
    implementation(project(":remote"))

    //Dagger - Hilt
    implementation(libraries["hilt_core"].toString())
    kapt(libraries["hilt_compiler"].toString())

    //Retrofit
    implementation(libraries["retrofit_core"].toString())
    implementation(libraries["retrofit_moshi_converter"].toString())
    implementation(libraries["retrofit_scalar_converter"].toString())

    implementation(libraries["kotlin_core"].toString())

    //Mockk
    testImplementation(libraries["mockk"].toString())

    testImplementation(libraries["junit"].toString())
    androidTestImplementation(libraries["test_junit"].toString())
    androidTestImplementation(libraries["espresso_core"].toString())
}