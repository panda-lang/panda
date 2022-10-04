dependencies {
    implementation(project(":panda-utilities"))

    implementation("org.ow2.asm:asm:9.3")
    implementation("org.panda-lang:expressible:1.2.1")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}