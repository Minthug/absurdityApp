import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.7.0"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.8.20"
	kotlin("plugin.spring") version "1.8.20"
	kotlin("plugin.jpa") version "1.4.32"
}

group = "absurdity"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

allOpen {
	annotation("jakarta.persistence.Entity")
	annotation("jakarta.persistence.MappedSuperclass")
	annotation("jakarta.persistence.Embeddable")
}


val mockkVersion = "1.9.2"
val fixtureVersion = "1.2.0"
val kotestVersion = "5.4.2"
val kotestExtentionVersion = "1.1.2"
val testingVersion = "7.7.0"
val springMockVersion = "4.0.2"


dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
	implementation("org.springframework.kafka:spring-kafka")
	implementation ("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
	implementation("com.fasterxml.jackson.core:jackson-databind")


	// Database
	implementation("org.jetbrains.exposed:exposed-spring-boot-starter:0.40.1")
	implementation("org.jetbrains.exposed:exposed-java-time:0.40.1")
	implementation("org.postgresql:postgresql:42.5.4")

	testImplementation("org.springframework.kafka:spring-kafka-test")

	testImplementation("io.mockk:mockk:${mockkVersion}")
	testImplementation("com.ninja-squad:springmockk:${springMockVersion}")
	testImplementation ("com.appmattus.fixture:fixture:${fixtureVersion}")

	testImplementation("io.kotest:kotest-runner-junit5:${kotestVersion}")
	testImplementation("io.kotest:kotest-assertions-core:${kotestVersion}")
	testImplementation("io.kotest.extensions:kotest-extensions-spring:${kotestExtentionVersion}")

	testImplementation("org.testng:testng:${testingVersion}")

	// Logging
	implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.mockk:mockk:1.13.4")
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
