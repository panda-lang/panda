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
    java.sourceCompatibility = JavaVersion.VERSION_1_8

    repositories {
        mavenLocal()

        maven {
            url = uri("https://maven.reposilite.com/maven-central")
        }

        maven {
            url = uri("https://repo.panda-lang.org/releases")
        }

        maven {
            url = uri("https://repo.maven.apache.org/maven2/")
        }
    }

    dependencies {
        val junit = "5.8.2"
        testImplementation("org.codehaus.groovy:groovy:3.0.9")
        testImplementation("org.junit.jupiter:junit-jupiter-api:$junit")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junit")
    }



    java {
        sourceCompatibility = java.sourceCompatibility
        targetCompatibility = java.sourceCompatibility

        withJavadocJar()
        withSourcesJar()
    }

}