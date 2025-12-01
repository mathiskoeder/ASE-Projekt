plugins {
    application
    java
}

group = "de.dhbw"
version = "1.0.0"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

application {
    mainClass.set("de.dhbw.chess.presentation.ConsoleApp")
}

repositories {
    mavenCentral()
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}
