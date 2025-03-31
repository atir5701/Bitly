package com.url.shortner.controller;

import com.url.shortner.dto.ClickEventDTO;
import com.url.shortner.dto.UrlMappingDTO;
import com.url.shortner.models.ClickEvent;
import com.url.shortner.models.User;
import com.url.shortner.service.UrlMappingService;
import com.url.shortner.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;

@RequestMapping("/api/urls")
@RestController
@AllArgsConstructor
public class UrlMappingController {

  @Autowired
  private UrlMappingService urlMappingService;

  @Autowired
  private UserService userService;

  @PostMapping("/shorten")
  public ResponseEntity<UrlMappingDTO> createShortUrl(@RequestBody
                                                      Map<String, String> request,
                                                      Principal principal) {
    String originalUrl = request.get("originalUrl");
    User user = userService.findByUserName(principal.getName());
    if (user == null) {
      return ResponseEntity.notFound().build();
    }
    UrlMappingDTO urlMapped = urlMappingService.createShortUrl(user, originalUrl);
    return ResponseEntity.ok(urlMapped);
  }

  @GetMapping("/getUrl")
  public ResponseEntity<List<UrlMappingDTO>> getMyUrl(Principal principal) {
    User user = userService.findByUserName(principal.getName());
    if (user == null) {
      return ResponseEntity.notFound().build();
    }
    List<UrlMappingDTO> myUrls = urlMappingService.findAllUrl(user);
    return ResponseEntity.ok(myUrls);
  }

  @GetMapping("/analytics/{shortUrl}")
  public ResponseEntity<List<ClickEventDTO>> getUrlAnalytics(@PathVariable String shortUrl,
                                                             @RequestParam("startDate") String startDate,
                                                             @RequestParam("endDate") String endDate) {
    DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    LocalDateTime start = LocalDateTime.parse(startDate, formatter);
    LocalDateTime end = LocalDateTime.parse(endDate, formatter);
    List<ClickEventDTO> clickEventDTOS = urlMappingService.getClickEventsByDate(shortUrl, start, end);
    return ResponseEntity.ok(clickEventDTOS);
  }

  @GetMapping("/totalClicks")
  public ResponseEntity<Map<LocalDate, Long>> getUrlAnalytics(@PathVariable String shortUrl,
                                                              Principal principal, @RequestParam("startDate") String startDate,
                                                              @RequestParam("endDate") String endDate) {
    User user = userService.findByUserName(principal.getName());
    DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
    LocalDate start = LocalDate.parse(startDate, formatter);
    LocalDate end = LocalDate.parse(endDate, formatter);
    Map<LocalDate,Long> totalClicks = urlMappingService.getTotalClicks(user, start, end);
    return ResponseEntity.ok(totalClicks);
  }
}
