import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.7.5"
	id("io.spring.dependency-management") version "1.0.15.RELEASE"
	id("io.gitlab.arturbosch.detekt") version "1.22.0-RC3"
	kotlin("jvm") version "1.7.21"
	kotlin("plugin.spring") version "1.6.21"
}

group = "reno.learn"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()

	jcenter {
		content {
			includeModule("org.jetbrains.kotlinx", "kotlinx-html-jvm")
		}
	}
}

/*
	Static code analyzer
 */

detekt {
	buildUponDefaultConfig = true // preconfigure defaults
	allRules = false // activate all available (even unstable) rules.
	config = files("$projectDir/config/detekt.yml") // point to your custom config defining rules to run, overwriting default behavior
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
	implementation("org.springframework.data:spring-data-mongodb")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.kotlintest:kotlintest-runner-junit5:3.4.2")
	testImplementation("io.mockk:mockk:1.13.2")
	testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
	detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.22.0-RC3")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
