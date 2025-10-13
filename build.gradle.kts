plugins {
    kotlin("jvm") version "2.2.20"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.xerial:sqlite-jdbc:3.43.0.0") //SQLite
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}