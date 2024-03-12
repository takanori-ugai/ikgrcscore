import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import io.gitlab.arturbosch.detekt.Detekt
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    kotlin("jvm") version "1.9.23"
    kotlin("kapt") version "1.9.23"
    java
    application
    jacoco
    id("io.gitlab.arturbosch.detekt") version "1.23.5"
    id("org.jetbrains.dokka") version "1.9.10"
//    id("com.github.sherter.google-java-format") version "0.9"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("org.jlleitschuh.gradle.ktlint") version "12.1.0"
    id("com.github.jk1.dependency-license-report") version "2.5"
    id("com.github.spotbugs") version "6.0.7"
    id("com.diffplug.spotless") version "6.25.0"
}

group = "com.fujitsu.labs.ikgrcscore"
version = "0.1"

repositories {
    mavenCentral()
}

dependencies {
//    implementation(kotlin("stdlib"))
    implementation("io.javalin:javalin:6.1.3")
    kapt("io.javalin.community.openapi:openapi-annotation-processor:6.1.3")
    implementation("io.javalin.community.openapi:javalin-openapi-plugin:6.1.3") // for /openapi route with JSON scheme
    implementation("io.javalin.community.openapi:javalin-swagger-plugin:6.1.2") // for Swagger UI
    implementation("io.javalin.community.openapi:javalin-redoc-plugin:6.1.3") // for ReDoc UI
    implementation("io.javalin:javalin-rendering:6.1.2")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.2")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.16.1")
    implementation("io.github.oshai:kotlin-logging-jvm:6.0.3")
    implementation("org.xerial:sqlite-jdbc:3.45.1.0")
    implementation("com.zaxxer:HikariCP:5.1.0")

//    runtimeOnly("org.slf4j:slf4j-simple:2.+")
    runtimeOnly("org.apache.logging.log4j:log4j-slf4j2-impl:2.+")
//    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
    testImplementation("io.javalin:javalin-testtools:6.1.3")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "17"
    }

    compileTestKotlin {
        kotlinOptions.jvmTarget = "17"
    }

    compileJava {
        options.encoding = "UTF-8"
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }

    compileTestJava {
        options.encoding = "UTF-8"
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }

    withType<Detekt>().configureEach {
        // Target version of the generated JVM bytecode. It is used for type resolution.
        jvmTarget = "17"
        reports {
            html.required.set(true) // observe findings in your browser with structure and code snippets
            xml.required.set(true) // checkstyle like format mainly for integrations like Jenkins
            // similar to the console output, contains issue signature to manually edit baseline files
            txt.required.set(true)
            /**
             * standardized SARIF format (https://sarifweb.azurewebsites.net/) to support integrations
             *  with Github Code Scanning
             */
            sarif.required.set(true)
        }
    }

    test {
        useJUnitPlatform()
        finalizedBy(jacocoTestReport) // report is always generated after tests run
    }

    jacocoTestReport {
        dependsOn(test) // tests are required to run before generating the report
    }

    withType<ShadowJar> {
        manifest {
            attributes["Main-Class"] = "com.fujitsu.labs.virtualhome.MainKt"
        }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

application {
    mainClass.set("com.fujitsu.ikgrcscore.AppKt")
}

detekt {
    buildUponDefaultConfig = true // preconfigure defaults
    allRules = false // activate all available (even unstable) rules.
    config.setFrom(files("$projectDir/config/detekt.yml"))
}

ktlint {
    verbose.set(true)
    outputToConsole.set(true)
    coloredOutput.set(true)
    reporters {
        reporter(ReporterType.CHECKSTYLE)
        reporter(ReporterType.JSON)
        reporter(ReporterType.HTML)
    }
    filter {
        exclude("**/style-violations.kt")
    }
}

spotbugs {
    ignoreFailures.set(true)
}

jacoco {
    toolVersion = "0.8.10"
//    reportsDirectory.set(layout.buildDirectory.dir("customJacocoReportDir"))
}

spotless {
    java {
        target("src/*/java/**/*.java")
        targetExclude("src/jte-classes/**/*.java", "jte-classes/**/*.java")
        // Use the default importOrder configuration
        importOrder()
        removeUnusedImports()

        // Choose one of these formatters.
        googleJavaFormat("1.18.1") // has its own section below
        formatAnnotations() // fixes formatting of type annotations, see below
    }
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of("17")) // "8"
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of("17")) // "8"
    }
}
