package com.url.shortner.service;

import com.url.shortner.dto.LoginRequest;
import com.url.shortner.models.User;
import com.url.shortner.repository.UserRepository;
import com.url.shortner.security.jwt.JwtAuthenticationResponse;
import com.url.shortner.security.jwt.JwtUtils;
 // Ensure this class exists

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
public class UserService {

  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private AuthenticationManager authenticationManager;
  @Autowired
  private JwtUtils jwtUtils;
  @Autowired
  private UserRepository userRepository;

  public User registerUser(User user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    return userRepository.save(user);
  }

  public JwtAuthenticationResponse loginUser(LoginRequest loginRequest) {
    Authentication auth = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(auth);

    UserDetialsImp userDetails = (UserDetialsImp) auth.getPrincipal();
    String jwt = jwtUtils.generateToken(userDetails);
    JwtAuthenticationResponse t = new JwtAuthenticationResponse();
    t.setToken(jwt);
    return t;

  }
}
