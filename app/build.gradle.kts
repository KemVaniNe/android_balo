plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.balo"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.balo"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.firestore)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation("androidx.databinding:viewbinding:8.3.2")
    implementation("com.airbnb.android:lottie:6.0.1")
    //firebase
    implementation(platform("com.google.firebase:firebase-bom:32.8.1"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")
    //glide
    implementation("com.github.bumptech.glide:glide:4.16.0")

    //BCrypt
    implementation("org.mindrot:jbcrypt:0.4")

    implementation("com.tbuonomo:dotsindicator:4.3")

    //Json
    implementation("com.google.code.gson:gson:2.10.1")

    //kotpref
    implementation("com.chibatching.kotpref:kotpref:2.13.1")
    implementation("com.chibatching.kotpref:enum-support:2.13.1")
    implementation("androidx.multidex:multidex:2.0.1")

    //chart
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")


}