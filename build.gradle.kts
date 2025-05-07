plugins {
    // Apply the org.jetbrains.kotlin.multiplatform plugin
    kotlin("multiplatform") version "1.9.20" apply false
    // Apply the org.jetbrains.kotlin.android plugin
    kotlin("android") version "1.9.20" apply false
    // Apply the Android application plugin
    id("com.android.application") version "8.1.0" apply false
    // Apply the Android library plugin
    id("com.android.library") version "8.1.0" apply false
    // Apply the Maven Publish plugin
    id("org.jetbrains.dokka") version "1.9.10" apply false
    id("io.github.gradle-nexus.publish-plugin") version "1.3.0" apply false
}

buildscript {
    dependencies {
        classpath("org.jetbrains.compose:compose-gradle-plugin:1.5.11")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
} 