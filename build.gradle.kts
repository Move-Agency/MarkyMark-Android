import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask

buildscript {
    repositories {
        google()
        mavenCentral()
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.detekt)
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.jvm) apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

// Detekt
subprojects {
    beforeEvaluate {
        apply(plugin = libs.plugins.detekt.get().pluginId)

        detekt {
            toolVersion = libs.versions.detekt.get()
            config = files("${rootProject.rootDir}/detekt-config.yml")
            buildUponDefaultConfig = true
            parallel = true
        }

        dependencies {
            detektPlugins(libs.detekt.formatting)
            detektPlugins("com.twitter.compose.rules:detekt:0.0.20")
        }

        tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
            reports {
                xml.required.set(true)
                html.required.set(false)
                txt.required.set(false)
                sarif.required.set(false)
                md.required.set(true)
            }
        }

        tasks.withType<DetektCreateBaselineTask>().configureEach {
            jvmTarget = libs.versions.jvm.get()
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
    delete(file("$rootProject/compose-metrics"))
}
