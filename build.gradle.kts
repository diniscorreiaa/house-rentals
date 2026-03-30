plugins {
    kotlin("jvm") version "2.3.0"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(group = "org.postgresql", name = "postgresql", version = "42.+")
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.2")
    //implementation("org.slf4j:slf4j-simple:2.0.9") // for logging

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.10.0")

    // Dependencies for HTTP4K
    implementation(platform("org.http4k:http4k-bom:6.34.0.0"))
    implementation("org.http4k:http4k-core")
    implementation("org.http4k:http4k-server-jetty")
}

tasks.register<Copy>("copyRuntimeDependencies") {
    into("build/libs")
    from(configurations.runtimeClasspath)
}
