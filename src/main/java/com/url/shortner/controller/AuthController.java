package com.url.shortner.controller;

import com.url.shortner.dto.LoginRequest;
import com.url.shortner.dto.RegisterRequest;
import com.url.shortner.models.User;
import com.url.shortner.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

  @Autowired
  private UserService userService;

  @PostMapping("/public/register")
  public ResponseEntity<?> registerUser(@RequestBody RegisterRequest req){
    User user = new User();
    user.setUsername(req.getUsername());
    user.setPassword(req.getPassword());
    user.setEmail(req.getEmail());
    user.setRole("ROLE_USER");
    user.setEmail(req.getEmail());
    userService.registerUser(user);
    return ResponseEntity.ok("User registered successfully");
  }

  @PostMapping("/public/login")
  public ResponseEntity<?> loginUser(@RequestBody LoginRequest req){
    return ResponseEntity.ok(userService.loginUser(req));
  }

}
