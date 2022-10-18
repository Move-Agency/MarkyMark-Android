plugins {
    `kotlin-dsl`
}

val jvmVersion = JavaVersion.VERSION_11

tasks.compileKotlin {
    kotlinOptions {
        jvmTarget = jvmVersion.toString()
    }
}

java {
    sourceCompatibility = jvmVersion
    targetCompatibility = jvmVersion
}

repositories {
    google()
    mavenCentral()
}
