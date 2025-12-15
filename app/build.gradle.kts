plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.lab_week_7"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.lab_week_7"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        // 🔐 SAFE injection API Key ke Manifest
        manifestPlaceholders["MAPS_API_KEY"] =
            project.findProperty("MAPS_API_KEY")?.toString() ?: ""
    }

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
}

dependencies {

    // AndroidX
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.activity:activity-ktx:1.9.3")
    implementation("androidx.fragment:fragment-ktx:1.8.5")

    // Google Maps SDK
    implementation("com.google.android.gms:play-services-maps:18.2.0")

    // Location Provider
    implementation("com.google.android.gms:play-services-location:21.3.0")

    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}
