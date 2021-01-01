buildscript {

    repositories {
        google()
        jcenter()
        mavenCentral()
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
    }


    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.72")
        classpath("com.android.tools.build:gradle:3.6.0")
        classpath ("com.google.gms:google-services:4.3.4")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()
        maven { setUrl("https://jitpack.io") }
    }
}

