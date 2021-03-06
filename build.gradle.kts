plugins {
    kotlin("multiplatform") version "1.4.31"
    id("com.android.library")
    id("kotlin-android-extensions")
}

val frameworkVersion = "1.0.0"
var frameworkName = "PokedexSDK"

group = "dev.mydearandroid"
version = frameworkVersion

repositories {
    google()
    jcenter()
    mavenCentral()
}

kotlin {
    android()
    iosArm64 { binaries.framework(frameworkName) }
    iosX64 { binaries.framework(frameworkName) }

    sourceSets {

        /**
         * Dependencias de uso comun
         **/
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        /**
         * Dependencias de uso para o Android
         **/
        val androidMain by getting {
            dependencies {
                implementation("com.google.android.material:material:1.2.1")
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("junit:junit:4.13")
            }
        }

        /**
         * Dependencias de uso para o iOS
         **/
        val iosMain by creating {
            dependencies { }
        }

        val iosTest by creating {
            dependencies { }
        }

        getByName("iosArm64Main") { dependsOn(iosMain) }
        getByName("iosArm64Test") { dependsOn(iosTest) }
        getByName("iosX64Main") { dependsOn(iosMain) }
        getByName("iosX64Test") { dependsOn(iosTest) }
    }

    tasks {
        val iosFrameworkName = frameworkName

        /**
         * Task para gerar o build em modo debug do SDK para iOS
         **/
        register("universalFrameworkDebug", org.jetbrains.kotlin.gradle.tasks.FatFrameworkTask::class) {
            baseName = iosFrameworkName
            from(
                iosArm64().binaries.getFramework(iosFrameworkName, "Debug"),
                iosX64().binaries.getFramework(iosFrameworkName, "Debug")
            )
            destinationDir = buildDir.resolve("bin/universal/debug")
            group = "Universal framework"
            description = "Builds a universal (fat) debug framework"
            dependsOn("link${iosFrameworkName}DebugFrameworkIosArm64")
            dependsOn("link${iosFrameworkName}DebugFrameworkIosX64")
        }

        /**
         * Task para gerar o build em modo release do SDK para iOS
         **/
        register("universalFrameworkRelease", org.jetbrains.kotlin.gradle.tasks.FatFrameworkTask::class) {
            baseName = iosFrameworkName
            from(
                iosArm64().binaries.getFramework(iosFrameworkName, "Release"),
                iosX64().binaries.getFramework(iosFrameworkName, "Release")
            )
            destinationDir = buildDir.resolve("bin/universal/release")
            group = "Universal framework"
            description = "Builds a universal (fat) release framework"
            dependsOn("link${iosFrameworkName}ReleaseFrameworkIosArm64")
            dependsOn("link${iosFrameworkName}ReleaseFrameworkIosX64")
        }

        /**
         * Task para chamada das tasks universalFrameworkDebug e universalFrameworkRelease
         **/
        register("universalFramework") {
            dependsOn("universalFrameworkDebug")
            dependsOn("universalFrameworkRelease")
        }
    }

    configure(listOf(targets["metadata"], android())) {
        mavenPublication {
            val targetPublication = this@mavenPublication
            tasks.withType<AbstractPublishToMaven>()
                .matching { it.publication == targetPublication }
        }
    }
}

/**
 * Configuracoes da configuracao da biblioteca para o Android.
 **/
android {
    compileSdkVersion(29)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdkVersion(24)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0.0"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}