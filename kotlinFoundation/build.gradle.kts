
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
    api("org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.serializationVersion}")
    api("com.squareup.okhttp3:okhttp:${Versions.okHttpVersion}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlinVersion}")

    testImplementation("junit:junit:${Versions.junitVersion}")
    testImplementation("io.mockk:mockk:1.10.0")
}