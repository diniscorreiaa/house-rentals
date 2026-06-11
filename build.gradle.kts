import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    kotlin("jvm") version "2.3.0"
    kotlin("plugin.serialization") version "2.3.0"

    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.postgresql:postgresql:42.+")
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
    description = "Copies the runtime dependencies to the build/libs directory."
    into("build/libs")
    from(configurations.runtimeClasspath)
}

tasks.named<Jar>("jar") {
    dependsOn("copyRuntimeDependencies")
    manifest {
        attributes["Main-Class"] = "pt.isel.ls.houses.server.ServerKt"

        // nome do projecto
        val jarName = archiveFileName.get()
        attributes["Class-Path"] = "$jarName " + configurations.runtimeClasspath.get().joinToString(" ") { it.name }
    }
}

// So that we don't have to spam our code with @OptIn(ExperimentalUuidApi::class)
// https://kotlinlang.org/docs/opt-in-requirements.html#opt-in-a-module
// https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.uuid/-experimental-uuid-api/
tasks.named<KotlinCompilationTask<*>>("compileKotlin").configure {
    compilerOptions.optIn.add("kotlin.uuid.ExperimentalUuidApi")
}


application {
    // Define the main class for the application.
    mainClass = "pt.isel.ls.houses.server.ServerKt"
}