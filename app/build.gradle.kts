import com.android.build.gradle.BaseExtension
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.googleServices)
    alias(libs.plugins.crashlytics)
    id("jacoco")
    id("org.sonarqube") version "6.1.0.5360"
}

sonar {
    properties {
        property("sonar.projectKey", "LucWaw_P15-Eventorias")
        property("sonar.organization", "lucwaw")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}

tasks.withType<Test> {
    extensions.configure(JacocoTaskExtension::class) {
        isIncludeNoLocationClasses = true
        excludes = listOf("jdk.internal.*")
    }
}

val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties()
try {
    keystoreProperties.load(FileInputStream(keystorePropertiesFile))
} catch (_: Exception) {
    //Do nothing
    println("Keystore properties file not found")
}

val androidExtension = extensions.getByType<BaseExtension>()

val jacocoTestReport by tasks.registering(JacocoReport::class) {
    dependsOn("testDebugUnitTest", "createDebugCoverageReport")
    group = "Reporting"
    description = "Generate Jacoco coverage reports"

    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    val debugTree = fileTree("${buildDir}/tmp/kotlin-classes/debug")
    val mainSrc = androidExtension.sourceSets.getByName("main").java.srcDirs

    classDirectories.setFrom(debugTree)
    sourceDirectories.setFrom(files(mainSrc))
    executionData.setFrom(fileTree(buildDir) {
        include("**/*.exec", "**/*.ec")
    })
}

android {
    signingConfigs {
        create("release") {
            storeFile = file("keystore/Eventorias_app_keystore_file.jks")
            storePassword = System.getenv("SIGNING_STORE_PASSWORD")
            keyAlias = System.getenv("SIGNING_KEY_ALIAS")
            keyPassword = System.getenv("SIGNING_KEY_PASSWORD")
        }
    }
    namespace = "com.openclassrooms.eventorias"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.openclassrooms.eventorias"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val keystoreFile = project.rootProject.file("apikey.properties")
        val properties = Properties()
        try {
            properties.load(keystoreFile.inputStream())
        } catch (_: Exception) {
            //Do nothing
            println("Keystore properties file not found")
        }

        //return empty key in case something goes wrong
        val apiKey = properties.getProperty("WEB_ID") ?: ""
        buildConfigField(
            type = "String",
            name = "WEB_ID",
            value = "\"$apiKey\""
        )
        val mapsKey = properties.getProperty("MAPS_API_KEY") ?: ""
        buildConfigField(
            type = "String",
            name = "MAPS_API_KEY",
            value = "\"$mapsKey\""
        )
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
        debug {
            enableAndroidTestCoverage = true
            enableUnitTestCoverage = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        isCoreLibraryDesugaringEnabled = true

    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    //To allow use of java date time api
    coreLibraryDesugaring(libs.desugar.jdk.libs)
    implementation(libs.coil.compose)

    implementation(libs.accompanist.permissions)

    //firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.ui.storage)
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.messaging.directboot)

    implementation(libs.androidx.credentials)


    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)

    //Koin
    implementation(libs.koin.android)
    implementation(libs.koin.compose)
    implementation(libs.koin.compose.viewmodel)
    implementation(libs.koin.compose.viewmodel.navigation)
    implementation(libs.koin.androidx.navigation)
    androidTestImplementation(libs.koin.test)
    androidTestImplementation(libs.koin.test.junit4)




    implementation(libs.navigation.compose)


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}