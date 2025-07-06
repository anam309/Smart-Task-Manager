plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.tasks.smarttaskmanager"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.tasks.smarttaskmanager"
        minSdk = 24
        targetSdk = 35
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
}
dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.room.runtime)
    implementation(libs.firebase.auth)
    implementation(platform("com.google.firebase:firebase-bom:33.16.0"))
    implementation("com.google.firebase:firebase-analytics")
    annotationProcessor(libs.room.compiler)
    testImplementation(libs.room.testing)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}