buildscript {

    repositories {
        google()
        jcenter()
        mavenCentral()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:4.1.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.21")
        classpath("org.jetbrains.kotlin:kotlin-serialization:1.4.21")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.38.1")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle.kts.kts files
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()
    }
}