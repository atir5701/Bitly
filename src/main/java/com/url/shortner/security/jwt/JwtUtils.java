package com.url.shortner.security.jwt;

import com.url.shortner.service.UserDetialsImp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class JwtUtils {

  @Value("${jwt.secret}")
  private String jwtSecret;

  private int jwtExpirationMs = 3600000;



  public String getJwtFromHeader(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if(bearerToken!=null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }

  public String generateToken(UserDetialsImp userDetails){
    String username = userDetails.getUsername();
    String roles = userDetails.getAuthorities().stream().map(authority->
            authority.getAuthority())
            .collect(Collectors.joining(","));
    return Jwts.builder()
            .setSubject(username)
            .claim("roles",roles)
            .setIssuedAt(new Date())
            .setExpiration(new Date((new Date().getTime() + jwtExpirationMs)))
            .signWith(key())
            .compact();
  }

  public String getUserNameFromToken(String token) {
    return Jwts.parserBuilder()
            .setSigningKey(key())
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
  }

  public boolean validateToken(String token) {

    try {
      Jwts.parserBuilder()
              .setSigningKey(key())  // Use the correct signing key
              .build()
              .parseClaimsJws(token);
      return true;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }


  private Key key(){
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
  }

}
