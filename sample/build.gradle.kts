@file:Suppress("UnstableApiUsage")

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.detekt)
}

android {
    namespace = "com.moveagency.markymark.sample"
    compileSdk = BuildConstants.TargetSdk

    defaultConfig {
        minSdk = BuildConstants.MinSdk
        targetSdk = BuildConstants.TargetSdk
        versionCode = BuildConstants.VersionCode
        versionName = BuildConstants.VersionName
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

    kotlin {
        jvmToolchain(libs.versions.jvm.get().toInt())
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + listOf(
            "-P",
            "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=${rootDir.absolutePath}/compose_metrics",
            "-P",
            "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=${rootDir.absolutePath}/compose_metrics"
        )
    }

    lint {
        xmlReport = true
        xmlOutput = file("${layout.buildDirectory}/reports/lint/lint-results-$name.xml")
        htmlReport = true
        htmlOutput = file("${layout.buildDirectory}/reports/lint/lint-results-$name.html")
        textReport = false
    }
}

dependencies {
    implementation(platform(libs.compose.bom))

    implementation(project(":markymark"))

    implementation(libs.immutable.collections)

    implementation(libs.core.ktx)
    implementation(libs.compose.activity)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    debugImplementation(libs.compose.ui.tooling)
    implementation(libs.compose.material3)

    implementation(libs.accompanist.systemuicontroller)
}
