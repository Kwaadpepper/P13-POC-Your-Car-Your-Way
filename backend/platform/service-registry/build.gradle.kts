plugins {
  alias(libs.plugins.spring.boot)
  id("java")
}

group = "ycyw.platform"
version = "0.0.1-SNAPSHOT"

dependencies {
  implementation(platform(libs.spring.boot.dependencies))
  implementation(platform(libs.spring.cloud.dependencies))

  implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-server")
  implementation("org.springframework.boot:spring-boot-starter-actuator")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
}
