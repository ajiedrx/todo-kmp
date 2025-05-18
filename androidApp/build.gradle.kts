plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    androidTarget()
    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation(project(":shared"))
            }
        }
    }
}

android {
    namespace = "com.adr.todo.android"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.adr.todo.android"
        minSdk = 21
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    sourceSets["main"].manifest.srcFile("src/main/AndroidManifest.xml")
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_18
        targetCompatibility = JavaVersion.VERSION_18
        isCoreLibraryDesugaringEnabled = true
    }

    kotlin {
        jvmToolchain(17)
    }
}

dependencies {
    implementation(projects.shared)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.android.compose.ui)
    implementation(libs.android.compose.ui.tooling)
    implementation(libs.android.compose.activity)
    implementation(libs.android.compose.material)
    implementation(libs.koin.android)
    coreLibraryDesugaring(libs.android.desugar.jdk)
}