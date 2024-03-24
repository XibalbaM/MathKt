plugins {
    kotlin("jvm") version "1.9.23"
    id("maven-publish")
}

group = "fr.xibalba"
version = "1.0.0"

repositories {
    mavenCentral()
    maven ("https://jitpack.io")
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("jitpack") {
                from(components["java"])
                groupId = "fr.xibalba"
                artifactId = "math"
                version = project.version.toString()
            }
        }
    }
}