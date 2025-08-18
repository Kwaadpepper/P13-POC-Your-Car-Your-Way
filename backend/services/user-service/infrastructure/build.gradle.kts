plugins {
  id("java")
}

group = "ycyw.services"
version = "0.0.1-SNAPSHOT"

repositories {
  mavenCentral()
  maven("https://repo.spring.io/release")
}

dependencies {
  implementation(project(":services:user-service:domain"))
  implementation(project(":shared"))

  // implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.3.0")
  // implementation("org.springframework.boot:spring-boot-starter-web:3.3.0")
}
