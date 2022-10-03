plugins {
    `maven-publish`
    `java-library`
    kotlin("jvm") version "1.7.10"
}

allprojects {

    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "maven-publish")
    apply(plugin = "java-library")

    group = "org.panda-lang"
    version = "0.5.2-alpha"
    java.sourceCompatibility = JavaVersion.VERSION_1_8

    repositories {
        mavenLocal()
        maven {
            url = uri("https://repo.panda-lang.org/releases")
        }

        maven {
            url = uri("https://repo.maven.apache.org/maven2/")
        }
    }

    dependencies {

        implementation(project(":panda-utilities"))

        implementation("net.dzikoysk:cdn:1.13.2") {
            exclude("org.panda-lang", "utilities")
        }
        implementation("org.panda-lang:expressible:1.2.1")

        implementation("org.javassist:javassist:3.28.0-GA")
        implementation("org.jetbrains:annotations:23.0.0")
        implementation("info.picocli:picocli:4.6.2")
        implementation("org.fusesource.jansi:jansi:2.4.0")

        val junit = "5.8.2"
        testImplementation("org.codehaus.groovy:groovy:3.0.9")
        testImplementation("org.junit.jupiter:junit-jupiter-api:$junit")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junit")

        testImplementation("org.openjdk.jmh:jmh-core:1.33")
        testImplementation("org.openjdk.jmh:jmh-generator-annprocess:1.33")
    }


    java {
        sourceCompatibility = java.sourceCompatibility
        targetCompatibility = java.sourceCompatibility

        withJavadocJar()
        withSourcesJar()
    }

}
