plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id ("com.google.dagger.hilt.android")

}

android {
    namespace = "com.milkman.demo.demomilkmaniii"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.milkman.demo.demomilkmaniii"
        minSdk = 24
        targetSdk = 33
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    val libraries: Map<String, Any> by project

    implementation(project(":ui"))

    //Dagger - Hilt
    implementation(libraries["hilt_core"].toString())
    kapt(libraries["hilt_compiler"].toString())

    implementation(libraries["kotlin_core"].toString())
    implementation(libraries["material"].toString())


    testImplementation(libraries["junit"].toString())
    androidTestImplementation(libraries["test_junit"].toString())
    androidTestImplementation(libraries["espresso_core"].toString())
}