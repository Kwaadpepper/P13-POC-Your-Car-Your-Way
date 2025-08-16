package com.ycyw.users.web;

import org.jspecify.annotations.Nullable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

  @GetMapping("/users/hello")
  public ResponseEntity<String> hello(
      @RequestParam(name = "name", required = false) @Nullable String name) {
    var who = (name == null || name.isBlank()) ? "user-service" : name;
    return ResponseEntity.ok("Hello from " + who);
  }
}
