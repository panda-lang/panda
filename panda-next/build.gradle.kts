java.sourceCompatibility = JavaVersion.VERSION_17
java.targetCompatibility = JavaVersion.VERSION_17

dependencies {
    implementation("org.ow2.asm:asm:9.3")
    implementation(project(":panda-utilities"))
    implementation("org.panda-lang:expressible:1.2.1")
}

description = "panda-next"
