package com.ycyw.users.web;

import org.jspecify.annotations.Nullable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for user-related endpoints.
 *
 * <p>Provides endpoints for user operations such as greeting.
 */
@RestController
public class UserController {

  /**
   * Returns a greeting message for the specified user name.
   *
   * @param name the name of the user to greet; if null or blank, defaults to "user-service"
   * @return a {@link ResponseEntity} containing the greeting message
   */
  @GetMapping("/users/hello")
  public ResponseEntity<String> hello(
      @RequestParam(name = "name", required = false) @Nullable String name) {
    var who = (name == null || name.isBlank()) ? "user-service" : name;
    return ResponseEntity.ok("Hello from " + who);
  }
}
