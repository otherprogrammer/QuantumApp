plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.quantumapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.quantumapp"
        minSdk = 26
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    // BOM de Compose para manejar las versiones de Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    // Material3 (ya debería estar manejado por el BOM si usas Compose)
    implementation(libs.androidx.material3)
    // Otras dependencias
    implementation("androidx.compose.material3:material3:1.3.2")
    implementation("androidx.navigation:navigation-compose:2.8.9") //para navegacion entre pantallas
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7") //para patron MVVM
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.7") //para patron MVVM
    implementation("io.coil-kt:coil-compose:2.5.0") //para imagenes o iconos externos desde la web
    implementation("com.google.accompanist:accompanist-navigation-animation:0.34.0") //para navegacion animada
    implementation("androidx.compose.material:material-icons-extended") // mas iconos
    // Test dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    // Debug dependencies
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}