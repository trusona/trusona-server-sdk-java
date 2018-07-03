package com.trusona.sdk.http.client.security;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class ParsedToken {
  @JsonProperty("sub")
  private UUID   subject;

  @JsonProperty("iss")
  private String   issuer;

  @JsonProperty("aud")
  private String audience;

  @JsonProperty("ath")
  private String authorities;

  @JsonProperty("exp")
  private Long expiredAt;

  @JsonProperty("iat")
  private Integer IssuedAt;

  @JsonProperty("jti")
  private UUID id;

  public UUID getSubject() {
    return subject;
  }

  public void setSubject(UUID subject) {
    this.subject = subject;
  }

  public String getIssuer() {
    return issuer;
  }

  public void setIssuer(String issuer) {
    this.issuer = issuer;
  }

  public String getAudience() {
    return audience;
  }

  public void setAudience(String audience) {
    this.audience = audience;
  }

  public String getAuthorities() {
    return authorities;
  }

  public void setAuthorities(String authorities) {
    this.authorities = authorities;
  }

  public Long getExpiredAt() {
    return expiredAt;
  }

  public void setExpiredAt(Long expiredAt) {
    this.expiredAt = expiredAt;
  }

  public Integer getIssuedAt() {
    return IssuedAt;
  }

  public void setIssuedAt(Integer issuedAt) {
    IssuedAt = issuedAt;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }
}
