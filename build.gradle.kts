import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    `maven-publish`
    `java-library`
    kotlin("jvm") version "1.7.10"
}

allprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "maven-publish")
    apply(plugin = "java-library")

    repositories {
        maven("https://maven.reposilite.com/maven-central")
        maven("https://repo.panda-lang.org/releases")
    }

    group = "org.panda-lang"
    version = "0.5.2-alpha"

    java {
        withJavadocJar()
        withSourcesJar()
    }
}

subprojects {
    dependencies {
        val junit = "5.8.2"
        testImplementation("org.codehaus.groovy:groovy:3.0.9")
        testImplementation("org.junit.jupiter:junit-jupiter-api:$junit")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junit")
    }

    tasks.withType<Test> {
        testLogging {
            events(
                TestLogEvent.STARTED,
                TestLogEvent.PASSED,
                TestLogEvent.FAILED,
                TestLogEvent.SKIPPED
            )
            exceptionFormat = TestExceptionFormat.FULL
            showExceptions = true
            showCauses = true
            showStackTraces = true
            showStandardStreams = true
        }

        maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2)
            .takeIf { it > 0 }
            ?: 1

        useJUnitPlatform()
    }
}