import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.4.2"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.4.21"
    kotlin("plugin.spring") version "1.4.21"
}

group = "pp.ua.ssenko"
version = "0.1"
java.sourceCompatibility = JavaVersion.VERSION_11

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
    maven("https://oss.jfrog.org/artifactory/oss-snapshot-local/")
}

val webappDir = "$projectDir/src/main/webapp"
sourceSets {
    main {
        resources {
            setSrcDirs(listOf("$webappDir/dist", "$projectDir/src/main/resources"))
        }
    }
}

tasks.processResources {
    dependsOn("buildAngular")
}

tasks.register<Exec>("buildAngular") {
    dependsOn("installAngular")
    workingDir(webappDir)
    inputs.dir(webappDir)
    // Add task to the standard build group
    group = BasePlugin.BUILD_GROUP
    // ng doesn't exist as a file in windows -> ng.cmd
    if (System.getProperty("os.name").toUpperCase().contains("WINDOWS")){
        commandLine("ng.cmd", "build")
    } else {
        commandLine("ng", "build")
    }
}

tasks.register<Exec>("installAngular") {
    workingDir(webappDir)
    inputs.dir(webappDir)
    group = BasePlugin.BUILD_GROUP
    if (System.getProperty("os.name").toUpperCase().contains("WINDOWS")){
        commandLine("npm.cmd", "install")
    } else {
        commandLine("npm", "install")
    }
}

dependencies {

    implementation("org.springdoc:springdoc-openapi-webflux-ui:1.4.3")

    implementation("org.springframework.boot:spring-boot-starter-rsocket")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
