plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    compileSdk = 33
    buildToolsVersion = "33.0.2"
    namespace = "app.revanced.integrations"

    defaultConfig {
        applicationId = "app.revanced.integrations"
        minSdk = 21
        targetSdk = 33
        multiDexEnabled = true
        versionName = project.version as String
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        applicationVariants.all {
            buildConfigField("String","VERSION_NAME","\"${defaultConfig.versionName}\"")
            outputs.all {
                this as com.android.build.gradle.internal.api.ApkVariantOutputImpl

                outputFileName = "${rootProject.name}-$versionName.apk"
            }
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility(JavaVersion.VERSION_11)
        targetCompatibility(JavaVersion.VERSION_11)
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation("androidx.core:core:1.0.0")
    implementation("androidx.appcompat:appcompat:1.0.0")
    compileOnly(project(mapOf("path" to ":dummy")))
    compileOnly("androidx.annotation:annotation:1.0.0")
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.2.2")
}

tasks.register("publish") { dependsOn("build") }
