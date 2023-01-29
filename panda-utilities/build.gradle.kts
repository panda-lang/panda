description = "Panda | Panda Utilities"

dependencies {
    implementation("org.panda-lang:expressible:1.2.9")
    implementation("org.javassist:javassist:3.28.0-GA")
    implementation("org.jetbrains:annotations:23.0.0")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}