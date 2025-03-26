import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    //id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("com.google.devtools.ksp") version "2.1.10-1.0.31"

}

val localProperties = Properties().apply {
    load(File(rootDir, "local.properties").inputStream())
}
val googleMapsApiKey: String = localProperties.getProperty("GOOGLE_MAPS_API_KEY") ?: ""


android {
    namespace = "com.example.mysafetracking"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.mysafetracking"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Google Maps
        buildConfigField("String", "MAPS_API_KEY", "\"$googleMapsApiKey\"")
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
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.navigation.runtime.android)
    implementation(libs.play.services.location)
    implementation(libs.firebase.crashlytics.buildtools)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Gif
    implementation(libs.coil.compose) // Gif
    // Para Coil 2.x
    implementation(libs.coil.kt.coil)
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("androidx.compose.material:material-icons-extended:1.5.4")

    // Google Maps
    implementation(libs.maps.compose)
    implementation(libs.play.services.maps)

    // Pack Icones
    implementation(libs.material.icons.extended)

    //Room
    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    implementation("androidx.room:room-ktx:2.5.0")
    annotationProcessor("androidx.room:room-compiler:$room_version") // If still using annotationProcessor
    ksp("androidx.room:room-compiler:$room_version") // For KSP
    ksp("androidx.room:room-ktx:$room_version")

    // Gson
    implementation("com.google.code.gson:gson:2.11.0")
}