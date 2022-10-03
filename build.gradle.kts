plugins {
    `maven-publish`
    `java-library`
    kotlin("jvm") version "1.7.10"
}

allprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "maven-publish")
    apply(plugin = "java-library")
}

subprojects {
    group = "org.panda-lang"
    version = "0.5.2-alpha"

    repositories {
        maven("https://maven.reposilite.com/maven-central")
        maven("https://repo.panda-lang.org/releases")
    }

    dependencies {
        val junit = "5.8.2"
        testImplementation("org.codehaus.groovy:groovy:3.0.9")
        testImplementation("org.junit.jupiter:junit-jupiter-api:$junit")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junit")
    }

    java {
        withJavadocJar()
        withSourcesJar()
    }
}