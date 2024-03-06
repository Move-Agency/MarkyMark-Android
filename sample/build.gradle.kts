@file:Suppress("UnstableApiUsage")

plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "com.moveagency.markymark.sample"
    compileSdk = Versions.TARGET_SDK

    defaultConfig {
        minSdk = Versions.MIN_SDK
        targetSdk = Versions.TARGET_SDK
        versionCode = Versions.VERSION_CODE
        versionName = Versions.VERSION_CODE.toString()
    }

    signingConfigs {
        create("release") {
            keyAlias = properties["markymark.keystore.keyalias"].toString()
            keyPassword = properties["markymark.keystore.keypassword"].toString()
            storeFile = file(rootDir.absolutePath + "/" + properties["markymark.keystore.path"].toString())
            storePassword = properties["markymark.keystore.password"].toString()
        }
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.findByName("release")
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = Versions.JVM
        targetCompatibility = Versions.JVM
    }

    kotlin {
        jvmToolchain(Versions.JVM.toString().toInt())
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Versions.KOTLIN_COMPILER_EXTENSION
    }

    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    kotlinOptions {
        jvmTarget = Versions.JVM.toString()
        freeCompilerArgs = freeCompilerArgs + listOf(
            "-P",
            "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=${rootDir.absolutePath}/compose_metrics",
            "-P",
            "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=${rootDir.absolutePath}/compose_metrics"
        )
    }

    lint {
        xmlReport = true
        xmlOutput = file("$buildDir/reports/lint/lint-results-$name.xml")
        htmlReport = true
        htmlOutput = file("$buildDir/reports/lint/lint-results-$name.html")
        textReport = false
    }
}

dependencies {
    implementation(platform("androidx.compose:compose-bom:2024.02.01"))

    implementation(project(":markymark"))

    implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:${Versions.IMMUTABLE}")

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.activity:activity-compose:${Versions.COMPOSE_ACTIVITY}")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    debugImplementation("androidx.compose.ui:ui-tooling")

    implementation("com.google.accompanist:accompanist-systemuicontroller:${Versions.ACCOMPANIST}")
}
