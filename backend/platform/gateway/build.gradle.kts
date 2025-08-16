plugins {
  alias(libs.plugins.spring.boot)
  id("java")
}

dependencies {
  implementation(platform(libs.spring.boot.dependencies))
  implementation(platform(libs.spring.cloud.dependencies))

  implementation("org.springframework.cloud:spring-cloud-starter-gateway")
  implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
  implementation("org.springframework.boot:spring-boot-starter-actuator")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
}
