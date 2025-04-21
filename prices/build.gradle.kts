plugins {
    id("java")
    id("org.springframework.boot") version "3.4.4"
    id("io.spring.dependency-management") version "1.0.15.RELEASE"
}

group = "org.alberto.mut"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    compileOnly("org.projectlombok:lombok:1.18.38")

    //DATABASE
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("com.h2database:h2")
    runtimeOnly("com.h2database:h2")

    //DOCUMENTATION
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0") // Use the latest version

    //TESTING
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.mockito:mockito-core:5.11.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.11.0")
    testImplementation("com.h2database:h2")

}

tasks.test {
    useJUnitPlatform()
}