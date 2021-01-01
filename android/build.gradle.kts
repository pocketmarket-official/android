plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-android-extensions")
    id("com.google.gms.google-services")
}

android {
    compileSdkVersion(28)
    buildToolsVersion = "28.0.3"

    defaultConfig {
        applicationId = "com.pocketmarket.Android"
        minSdkVersion(21)
        targetSdkVersion(28)
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true

        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }

    buildTypes {

        getByName("debug") {
            isMinifyEnabled = false
            isDebuggable = true
            buildConfigField("String", "entryUrl", "\"http://localhost:3000\"")
            // buildConfigField("String", "entryUrl", "\"http://localhost:3000\"")
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }

        getByName("release") {
            isMinifyEnabled = false
            isDebuggable = false
            buildConfigField("String", "entryUrl", "\"http://13.124.90.138:3000\"")
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }

        /*getByName("devDebug") {
            isMinifyEnabled = false
            isDebuggable = false

        }*/


    }

    compileOptions {
        setSourceCompatibility(1.8)
        setTargetCompatibility(1.8)
    }
}

dependencies {
//    implementation(fileTree(dir: 'libs', include: ['*.jar']))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.3.72")

    implementation("androidx.appcompat:appcompat:1.0.2")
    implementation("androidx.constraintlayout:constraintlayout:2.0.0")
    implementation(platform("com.google.firebase:firebase-bom:26.1.1"))

    // Declare the dependencies for the Firebase Cloud Messaging and Analytics libraries
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation ("com.google.firebase:firebase-messaging-ktx")
    implementation ("com.google.firebase:firebase-analytics-ktx")
}

