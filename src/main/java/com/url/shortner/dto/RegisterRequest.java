package com.url.shortner.dto;

import org.springframework.stereotype.Service;

import java.util.Set;

import lombok.Data;

@Service
@Data
public class RegisterRequest {
  private String username;
  private String password;
  private String email;
  private Set<String> role;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Set<String> getRole() {
    return role;
  }

  public void setRole(Set<String> role) {
    this.role = role;
  }
}
