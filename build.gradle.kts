import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask

buildscript {

    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:${Versions.GRADLE_PLUGIN}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.KOTLIN}")
    }
}

plugins {
    id("io.gitlab.arturbosch.detekt") version Versions.DETEKT
}

allprojects {

    repositories {
        google()
        mavenCentral()
    }
}

// Detekt

subprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")

    detekt {
        toolVersion = Versions.DETEKT
        config = files("${rootProject.rootDir}/detekt-config.yml")
        buildUponDefaultConfig = true
        parallel = true
    }

    dependencies {
        detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:${Versions.DETEKT}")
        detektPlugins("com.braisgabin.detekt:kotlin-compiler-wrapper:0.0.2")
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
        this.jvmTarget = Versions.JVM.toString()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
