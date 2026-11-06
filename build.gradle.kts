plugins {
    kotlin("jvm") version "2.4.20"
    kotlin("plugin.serialization") version "2.4.20"
    application
}

group = "com.adoptia.backend"
version = "0.0.1"

application {
    mainClass.set("com.adoptia.backend.ApplicationKt")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core:2.3.11")
    implementation("io.ktor:ktor-server-netty:2.3.11")
    implementation("io.ktor:ktor-server-content-negotiation:2.3.11")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.11")
    implementation("io.ktor:ktor-server-call-logging:2.3.11")
    implementation("io.ktor:ktor-server-cors:2.3.11")
    implementation("ch.qos.logback:logback-classic:1.5.6")

    // Driver para conectarse a la base de datos MySQL en Railway
    implementation("com.mysql:mysql-connector-j:8.4.0")
}