@file:Suppress("UnstableApiUsage")

plugins {
    id("com.android.library")
    kotlin("android")
    id("maven-publish")
}

android {
    namespace = Versions.NAMESPACE
    compileSdk = Versions.TARGET_SDK

    defaultConfig {
        minSdk = Versions.MIN_SDK
        targetSdk = Versions.TARGET_SDK
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = Versions.JVM
        targetCompatibility = Versions.JVM
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Versions.KOTLIN_COMPILER_EXTENSION
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }

    kotlin {
        jvmToolchain(Versions.JVM.toString().toInt())
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
        xmlOutput = file("$buildDir/reports/lint/lint-results-$name.xml")
        htmlReport = true
        htmlOutput = file("$buildDir/reports/lint/lint-results-$name.html")
        textReport = false
    }
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("mavenRelease") {
                groupId = Versions.NAMESPACE
                version = Versions.VERSION_NAME
                from(components.getByName("release"))
                pom {
                    name.set("MarkyMark-Android")
                    description.set("Library for converting Markdown to Android Jetpack Compose elements")
                    url.set("https://git.m2mobi.com/projects/ML/repos/m2utility/browse")
                    licenses {
                        license {
                            name.set("The MIT License (MIT)")
                            url.set("https://raw.githubusercontent.com/Move-Agency/MarkyMark-Android/v3/develop/LICENSE")
                        }
                    }
                    organization {
                        name.set("Move Agency")
                        url.set("https://www.moveagency.com/")
                    }
                }
            }
        }
    }
}

dependencies {
    implementation(platform("androidx.compose:compose-bom:2024.02.01"))

    implementation("androidx.appcompat:appcompat:1.6.1")

    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    debugImplementation("androidx.compose.ui:ui-tooling")

    implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:${Versions.IMMUTABLE}")

    implementation("com.vladsch.flexmark:flexmark:${Versions.FLEXMARK}")
    implementation("com.vladsch.flexmark:flexmark-ext-autolink:${Versions.FLEXMARK}")
    implementation("com.vladsch.flexmark:flexmark-ext-gfm-strikethrough:${Versions.FLEXMARK}")
    implementation("com.vladsch.flexmark:flexmark-ext-gfm-tasklist:${Versions.FLEXMARK}")
    implementation("com.vladsch.flexmark:flexmark-ext-superscript:${Versions.FLEXMARK}")
    implementation("com.vladsch.flexmark:flexmark-ext-tables:${Versions.FLEXMARK}")

    implementation("io.coil-kt:coil:${Versions.COIL_KT}")
    implementation("io.coil-kt:coil-compose:${Versions.COIL_KT}")
}
