
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
    alias(libs.plugins.kotlinSerialization)
}

dependencies {
    coreLibraryDesugaring(libs.android.desugar.jdk)
}

kotlin {
    jvmToolchain(21)

    androidTarget()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    targets.configureEach {
        compilations.configureEach {
            compileTaskProvider.get().compilerOptions {
                freeCompilerArgs.add("-Xexpect-actual-classes")
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.material)
                implementation(compose.materialIconsExtended)
                implementation(libs.kotlinx.datetime)
                implementation(libs.kotlinx.coroutine.core)
                implementation(libs.androidx.room.runtime)
                implementation(libs.sqlite.bundled)
                implementation(libs.koin.core)
                implementation(libs.common.viewmodel)
                implementation(libs.koin.compose.mp)
                implementation(libs.navigation.compose)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.androidx.core.ktx)
                implementation(libs.compose.preview)
                implementation(compose.uiTooling)
                implementation(libs.koin.android)
            }
        }

        val iosMain by creating {
            dependsOn(commonMain)
        }
        val iosX64Main by getting {
            dependsOn(iosMain)
        }
        val iosArm64Main by getting {
            dependsOn(iosMain)
        }
        val iosSimulatorArm64Main by getting {
            dependsOn(iosMain)
        }
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspIosX64", libs.androidx.room.compiler)
    add("kspIosArm64", libs.androidx.room.compiler)
    add("kspIosSimulatorArm64", libs.androidx.room.compiler)

    debugImplementation(libs.android.compose.ui.tooling)
}

android {
    namespace = "com.adr.todo.shared"
    compileSdk = 35
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
    }
    sourceSets["main"].manifest.srcFile("src/main/AndroidManifest.xml")
    defaultConfig {
        minSdk = 21
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}
