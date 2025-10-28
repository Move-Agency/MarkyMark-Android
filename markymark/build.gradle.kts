/*
 * Copyright © 2025 Framna
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the “Software”), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 * the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */

@file:Suppress("UnstableApiUsage")

import Publish.addPublishRepository
import Publish.setup

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.detekt)
    alias(libs.plugins.compose.compiler)
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
        jvmTarget = "17"
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
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
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

    api(libs.kodeview)
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("mavenRelease") {
                setup(project)
            }
        }
        addPublishRepository(project)
    }
}
