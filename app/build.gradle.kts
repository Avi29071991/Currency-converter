plugins {
    id("com.android.application")
    id("dagger.hilt.android.plugin")
    id("kotlin-android")
    id("kotlin-kapt")
}

android {
    compileSdkVersion(30)
    buildToolsVersion("30.0.3")

    defaultConfig {
        applicationId = "com.avinash.paypay.test.currencyconverter"
        minSdkVersion(28)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0.0"
        testInstrumentationRunner("androidx.test.runner.AndroidJUnitRunner")
    }

    buildFeatures {
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(":kotlinFoundation"))
    implementation(project(":featureCurrency"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlinVersion}")
    implementation("androidx.core:core-ktx:${Versions.coreKtxVersion}")
    implementation("androidx.appcompat:appcompat:${Versions.appCompatVersion}")
    implementation ("com.google.android.material:material:${Versions.materialVersion}")
    implementation ("androidx.constraintlayout:constraintlayout:${Versions.constraintLayoutVersion}")
    implementation("androidx.startup:startup-runtime:${Versions.startUpVersion}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutineVersions}")

    // Dependency for Work Manager
    implementation("androidx.work:work-runtime:${Versions.workManagerVersion}")

    // Dependencies for Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:${Versions.navigationVersion}")
    implementation("androidx.navigation:navigation-ui-ktx:${Versions.navigationVersion}")

    // Dependencies for ViewModel and LiveData
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifeCycleVersion}")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifeCycleVersion}")
    implementation("androidx.lifecycle:lifecycle-process:${Versions.lifeCycleVersion}")
    implementation("androidx.lifecycle:lifecycle-common-java8:${Versions.lifeCycleVersion}")

    // Dependencies for Room
    implementation("androidx.room:room-runtime:${Versions.roomVersion}")
    implementation("androidx.room:room-ktx:${Versions.roomVersion}")
    kapt("androidx.room:room-compiler:${Versions.roomVersion}")

    // Dependencies for Hilt
    implementation("com.google.dagger:hilt-android:${Versions.hiltVersion}")
    kapt("com.google.dagger:hilt-compiler:${Versions.hiltVersion}")

    // Dependencies for Hilt using WorkManager
    implementation("androidx.hilt:hilt-work:${Versions.hiltWorkManagerVersion}")
    kapt("androidx.hilt:hilt-compiler:${Versions.hiltWorkManagerVersion}")

    // For local unit tests
    testImplementation ("junit:junit:${Versions.junitVersion}")
    androidTestImplementation ("androidx.test.ext:junit:${Versions.androidJunitExtensionVersion}")
    androidTestImplementation ("androidx.test.espresso:espresso-core:${Versions.espressoVersion}")
}