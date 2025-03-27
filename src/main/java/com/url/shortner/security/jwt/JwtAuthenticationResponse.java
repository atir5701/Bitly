package com.url.shortner.security.jwt;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@NoArgsConstructor
public class JwtAuthenticationResponse {
  private String token;


  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }
}
