package com.url.shortner.security.jwt;

import com.url.shortner.service.UserDetailsServiceImplementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  @Autowired
  private JwtUtils jwtTokenProvider;

  @Autowired
  private UserDetailsService userDetailsService;


  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain)
          throws ServletException, IOException {
    try{
      String jwt = jwtTokenProvider.getJwtFromHeader(request);
      if(jwt!=null && jwtTokenProvider.validateToken(jwt)){
        String username = jwtTokenProvider.getUserNameFromToken(jwt);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if(userDetails != null){
          UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails,
                  null, userDetails.getAuthorities());
          auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(auth);
        }
      }
    }catch (Exception e){
      e.printStackTrace();
    }

    filterChain.doFilter(request,response);
  }
}
