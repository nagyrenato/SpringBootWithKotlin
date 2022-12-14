import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.7.5"
    id("io.spring.dependency-management") version "1.0.15.RELEASE"
    id("io.gitlab.arturbosch.detekt") version "1.22.0-RC3"
    kotlin("jvm") version "1.7.21"
    kotlin("plugin.spring") version "1.7.21"
    /*
        Jacoco is extremely helpful for example, a lot of time it turned out
        I can use val instead of var in the constructor parameter, and it was shown in the
        Jacoco report as the setters were not covered.
     */
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
    /*
        Annotations like
        import javax.validation.Valid
        import javax.validation.constraints.NotEmpty
        requires this dependency, without it the project will build and run but the validation won't happen
     */
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.springframework.data:spring-data-mongodb")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.4")
    implementation("io.springfox:springfox-swagger-ui:3.0.0")
    implementation("io.springfox:springfox-swagger2:3.0.0")
    implementation("org.springdoc:springdoc-openapi-ui:1.6.13")
    implementation("org.springdoc:springdoc-openapi-data-rest:1.6.13")
    implementation("org.springdoc:springdoc-openapi-kotlin:1.6.13")
    /*
        The project uses JUnit4 due to the following issue:
        https://github.com/gradle/gradle/issues/6453
     */
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.kotlintest:kotlintest-runner-junit4:3.4.2")
    testImplementation("io.kotest:kotest-extensions-spring:4.4.3")
    testImplementation("org.mockito:mockito-core:4.9.0")
    testImplementation("org.mockito:mockito-inline:4.9.0")
    testImplementation("io.mockk:mockk:1.13.2")
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:mongodb:1.17.6")
    /*
        Vulnerabilities at this moment but there's no newer version
     */
    testImplementation("io.rest-assured:spring-mock-mvc:5.3.0")
    /*
        Plugins
     */
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.22.0")
}

dependencyManagement {
    imports {
        mavenBom("org.testcontainers:testcontainers-bom:1.17.6")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

val ignoredPaths: Iterable<String> = listOf(
    "reno/learn/kotlin/KotlinExampleApplication*"
)

tasks.test {
    useJUnit()
    finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
}
tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report
}

tasks.jacocoTestReport {
    reports {
        xml.required.set(true)
        csv.required.set(true)
    }
    classDirectories.setFrom(
        sourceSets.main.get().output.asFileTree.matching {
            exclude(ignoredPaths)
        }
    )
}

tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            limit {
                minimum = "0.8".toBigDecimal()
            }
        }
    }
    mustRunAfter(tasks["jacocoTestReport"])
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
}
tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report
}
