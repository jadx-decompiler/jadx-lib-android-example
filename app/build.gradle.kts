plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "io.github.skylot.jadx.android.example"
    compileSdk = 34

    defaultConfig {
        applicationId = "io.github.skylot.jadx.android.example"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
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
        compose = true
    }
}

dependencies {
    val jadxVersion = "1.5.1-SNAPSHOT"
    val isJadxSnapshot = jadxVersion.endsWith("-SNAPSHOT")

    // use compile only scope to exclude jadx-core and its dependencies from result jar
    implementation("io.github.skylot:jadx-core:$jadxVersion") {
        isChanging = isJadxSnapshot
    }
    implementation("io.github.skylot:jadx-dex-input:$jadxVersion") {
        isChanging = isJadxSnapshot
    }

    // Important: add slf4j implementation.
    // Jadx uses threads for processing and some exceptions will not propagate to lib caller.
    // Log is the only way to see such errors.
    // You can use any other slf4j implementation if you want to :)
    implementation("com.github.tony19:logback-android:3.0.0")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.material3)
}
