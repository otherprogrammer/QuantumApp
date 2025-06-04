plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.gms.google.services)
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
    // Firebase BOM para manejar las versiones de Firebase
    implementation(platform("com.google.firebase:firebase-bom:30.3.0"))
    // Dependencias específicas de Firebase sin especificar versiones
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-database")
    // Otras dependencias
    implementation("androidx.compose.material3:material3:1.3.2")
    implementation("androidx.navigation:navigation-compose:2.8.9") //para navegacion entre pantallas
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7") //para patron MVVM
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.7") //para patron MVVM
    implementation("io.coil-kt:coil-compose:2.5.0") //para imagenes o iconos externos desde la web
    implementation("com.google.accompanist:accompanist-navigation-animation:0.34.0") //para navegacion animada
    implementation("androidx.compose.material:material-icons-extended") // mas iconos
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("androidx.compose.foundation:foundation:1.6.7")

    // ExoPlayer
    implementation("androidx.media3:media3-exoplayer:1.3.1") // Reproductor principal
    implementation("androidx.media3:media3-ui:1.3.1") // Componentes de UI de ExoPlayer (aunque no los usaremos directamente para el fondo)
    implementation("androidx.media3:media3-common:1.3.1") // Utilidades comunes

    // Lottie para animaciones
    implementation("com.airbnb.android:lottie-compose:6.4.0")

    // WebView
    implementation("androidx.webkit:webkit:1.11.0")

    // Accompanist completo
    implementation("com.google.accompanist:accompanist-flowlayout:0.34.0")
    implementation("com.google.accompanist:accompanist-pager:0.34.0")
    implementation("com.google.accompanist:accompanist-pager-indicators:0.34.0")
    implementation("com.google.accompanist:accompanist-placeholder-material:0.34.0")
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.34.0")

    // Motion Compose
    implementation("androidx.compose.animation:animation:1.6.1")
    implementation("androidx.compose.animation:animation-core:1.6.1")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")

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

apply(plugin = "com.google.gms.google-services")