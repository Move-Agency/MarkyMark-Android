@file:Suppress("UnstableApiUsage")

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.detekt)
    id("maven-publish")
}

android {
    namespace = BuildConstants.Namespace
    compileSdk = BuildConstants.TargetSdk

    defaultConfig {
        minSdk = BuildConstants.MinSdk
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }

    kotlin {
        jvmToolchain(libs.versions.jvm.get().toInt())
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

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("mavenRelease") {
                groupId = BuildConstants.Namespace
                version = BuildConstants.VersionName
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
    implementation(libs.appcompat)

    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    debugImplementation(libs.compose.ui.tooling)
    implementation(libs.compose.material3)

    implementation(libs.immutable.collections)

    implementation(libs.flexmark)
    implementation(libs.flexmark.autolink)
    implementation(libs.flexmark.gfm.strikethrough)
    implementation(libs.flexmark.gfm.tasklist)
    implementation(libs.flexmark.superscript)
    implementation(libs.flexmark.tables)

    implementation(libs.coil)
    implementation(libs.coil.compose)
}
