import com.m2mobi.m2ci.extension.model.BuildVariant
import com.m2mobi.m2ci.task.distribute.model.DistributionDestination
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    alias(libs.plugins.m2ci)
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.detekt)
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.jvm) apply false
}

ci {
    jvmVersion = JavaVersion.toVersion(libs.versions.jvm.get())
    projectName = BuildConstants.ProjectName
    projectVersion = BuildConstants.VersionName

    buildConfiguration {
        all { mainVariant = BuildVariant(buildType = "release") }
    }

    assemble {
        enable = true
        versionMetadataBuilders = emptySet()
    }

    distribute {
        enable = true
        destination = DistributionDestination.Library
    }

    sonarqube {
        enable = false
    }
    // sonarqube {
    //     projectKey = BuildConstants.ProjectKey
    //     coverageExclusions = SonarQubeTask.DEFAULT_COVERAGE_EXCLUSIONS + setOf(
    //         "view/**",
    //     )
    //     exclusions = SonarQubeTask.DEFAULT_EXCLUSIONS + setOf(
    //         "**/src/jvmTest/**",
    //     )
    // }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

subprojects {
    tasks.withType(KotlinCompile::class) {
        compilerOptions.freeCompilerArgs.addAll(
            "-Xcontext-receivers",
            "-Xskip-prerelease-check",
            "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
        )
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
            detektPlugins("ru.kode:detekt-rules-compose:1.3.0")
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
