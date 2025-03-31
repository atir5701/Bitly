package com.url.shortner.service;

import com.url.shortner.dto.ClickEventDTO;
import com.url.shortner.dto.UrlMappingDTO;
import com.url.shortner.models.ClickEvent;
import com.url.shortner.models.UrlMapping;
import com.url.shortner.models.User;
import com.url.shortner.repository.ClickEventRepository;
import com.url.shortner.repository.UrlMappingRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.method.AuthorizeReturnObject;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class UrlMappingService {

  @Autowired
  UrlMappingRepository urlMappingRepository;

  @Autowired
  ClickEventRepository clickEventRepository;



  public UrlMappingDTO createShortUrl(User user, String originalUrl){
    String shorUrl = generateShortUrl(originalUrl);
    UrlMapping urlMapping = new UrlMapping();
    urlMapping.setShortUrl(shorUrl);
    urlMapping.setUser(user);
    urlMapping.setOriginalUrl(originalUrl);
    urlMapping.setCreatedAt(LocalDateTime.now());
    UrlMapping saveUrlMapping = urlMappingRepository.save(urlMapping);
    return  convertToDto(saveUrlMapping);
  }

  private UrlMappingDTO convertToDto(UrlMapping urlMapping){
    UrlMappingDTO urlMappingDTO = new UrlMappingDTO();
    urlMappingDTO.setShortUrl(urlMapping.getShortUrl());
    urlMappingDTO.setOriginalUrl(urlMapping.getOriginalUrl());
    urlMappingDTO.setCreatedAt(urlMapping.getCreatedAt());
    urlMappingDTO.setId(urlMapping.getId());
    urlMappingDTO.setUsername(urlMapping.getUser().getUsername());
    return urlMappingDTO;
  }

  private String generateShortUrl(String originalUrl) {
    String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    Random random = new Random();
    StringBuilder shortUrl = new StringBuilder(8);
    for(int i=0;i<8;i++){
      shortUrl.append(characters.charAt(random.nextInt(characters.length())));
    }
    return shortUrl.toString();
  }

  public List<UrlMappingDTO> findAllUrl(User user) {
    List<UrlMapping> allUrlMappings = urlMappingRepository.findByUser(user);
    List<UrlMappingDTO> urlMappingDTOS = new ArrayList<>();
    for(int i=0;i<allUrlMappings.size();i++){
      urlMappingDTOS.add(convertToDto(allUrlMappings.get(i)));
    }
    return urlMappingDTOS;
  }

  public List<ClickEventDTO> getClickEventsByDate(String shortUrl, LocalDateTime start, LocalDateTime end) {
      UrlMapping urlMapping = urlMappingRepository.findByShortUrl(shortUrl);
      if(urlMapping == null){
        return null;
      }
      return clickEventRepository.findByUrlMappingAndClickDateBetween(urlMapping, start, end)
              .stream()
              .collect(Collectors.groupingBy(click -> click.getClickDate().toLocalDate(),
                      Collectors.counting()))
              .entrySet().stream()
              .map(entry->{
                ClickEventDTO clickEventDTO = new ClickEventDTO();
                clickEventDTO.setClickDate(entry.getKey());
                clickEventDTO.setCount(entry.getValue());
                return clickEventDTO;
              })
              .collect(Collectors.toList());

  }

  public Map<LocalDate, Long> getTotalClicks(User user, LocalDate start, LocalDate end) {
      List<UrlMapping> userUrls = urlMappingRepository.findByUser(user);
      List<ClickEvent> clickEvents = clickEventRepository.findByUrlMappingInAndClickDateBetween(userUrls, start.atStartOfDay(), end.plusDays(1).atStartOfDay());
      return clickEvents.stream()
              .collect(Collectors.groupingBy(click -> click.getClickDate().toLocalDate(),
                      Collectors.counting()));


  }

  public UrlMapping getOriginalUrl(String shortUrl){
    UrlMapping temp = urlMappingRepository.findByShortUrl(shortUrl);
    if(temp!=null){
      temp.setClickCount(temp.getClickCount()+1);
      urlMappingRepository.save(temp);
      ClickEvent clickEvent = new ClickEvent();
      clickEvent.setUrlMapping(temp);
      clickEvent.setClickDate(LocalDateTime.now());
      clickEventRepository.save(clickEvent);
    }
    return temp;
  }

}
