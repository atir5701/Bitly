package com.url.shortner.models;


import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name= "url_mapping")
public class UrlMapping {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String originalUrl;
  private String shortUrl;
  private int  clickCount = 0;
  private LocalDateTime createdAt;

  @ManyToOne
  @JoinColumn(name="user_id")
  private User user;

  @OneToMany(mappedBy = "urlMapping")
  private List<ClickEvent> clickEvents;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getOriginalUrl() {
    return originalUrl;
  }

  public void setOriginalUrl(String originalUrl) {
    this.originalUrl = originalUrl;
  }

  public String getShortUrl() {
    return shortUrl;
  }

  public void setShortUrl(String shortUrl) {
    this.shortUrl = shortUrl;
  }

  public int getClickCount() {
    return clickCount;
  }

  public void setClickCount(int clickCount) {
    this.clickCount = clickCount;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public List<ClickEvent> getClickEvents() {
    return clickEvents;
  }

  public void setClickEvents(List<ClickEvent> clickEvents) {
    this.clickEvents = clickEvents;
  }
}
