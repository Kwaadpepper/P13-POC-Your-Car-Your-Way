package com.ycyw.users.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
  @GetMapping("/users/hello")
  public ResponseEntity<String> hello() {
    return ResponseEntity.ok("Hello from user-service");
  }
}
