import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.7.5"
    id("io.spring.dependency-management") version "1.0.15.RELEASE"
    id("io.gitlab.arturbosch.detekt") version "1.22.0-RC3"
    kotlin("jvm") version "1.7.21"
    kotlin("plugin.spring") version "1.7.21"
    jacoco
}

group = "reno.learn"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

/*
    Static code analyzer
 */
detekt {
    buildUponDefaultConfig = true // preconfigure defaults
    allRules = false // activate all available (even unstable) rules.
    // point to  custom config defining rules to run, overwriting default behavior
    config = files("$projectDir/config/detekt.yml")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter") {}
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.springframework.data:spring-data-mongodb")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.4")
    implementation("de.flapdoodle.embed:de.flapdoodle.embed.mongo:3.5.1")
    /*
    The project uses JUnit4 due to the following issue:
    https://github.com/gradle/gradle/issues/6453
     */
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.kotlintest:kotlintest-runner-junit4:3.4.2")
    testImplementation("io.mockk:mockk:1.13.2")
    testImplementation("junit:junit:4.13.2")
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.22.0-RC3")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnit()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report
}
