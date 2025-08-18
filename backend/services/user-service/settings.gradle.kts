rootProject.name = "user-service"

include(
  "services:user-service:application",
  "services:user-service:domain",
  "services:user-service:infrastructure",
)
