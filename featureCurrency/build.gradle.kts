plugins {
    id("java-library")
    id("kotlin")
    kotlin("plugin.serialization")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    implementation(project(":kotlinFoundation"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlinVersion}")
    testImplementation("com.squareup.okhttp3:mockwebserver:${Versions.okHttpVersion}")
}