package com.trusona.sdk.http.client.v2.response;

import com.trusona.sdk.resources.dto.BaseDto;

import java.util.Date;
import java.util.UUID;

import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;

/**
 * Copyright Trusona, Inc. Created on 1/22/18 for trusona-server-sdk.
 */
public class TrusonaficationResponse extends BaseDto {

  private static final long serialVersionUID = 3555946086952925089L;

  private String deviceIdentifier;
  private String email;
  private int desiredLevel;
  private String action;
  private String resource;
  private Date expiresAt;
  private boolean userPresence;
  private boolean prompt;
  private boolean showIdentityDocument;

  private UUID id;
  private String status;
  private String userIdentifier;
  private String trusonaId;
  private Date createdAt;
  private Date updatedAt;
  private TrusonaficationResultResponse result;

  public TrusonaficationResponse() {
    userPresence = true;
    prompt = true;
  }

  public String getDeviceIdentifier() {
    return deviceIdentifier;
  }

  public void setDeviceIdentifier(String deviceIdentifier) {
    this.deviceIdentifier = deviceIdentifier;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public int getDesiredLevel() {
    return desiredLevel;
  }

  public void setDesiredLevel(int desiredLevel) {
    this.desiredLevel = desiredLevel;
  }

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public String getResource() {
    return resource;
  }

  public void setResource(String resource) {
    this.resource = resource;
  }

  public Date getExpiresAt() {
    return expiresAt != null ? new Date(expiresAt.getTime()) : null;
  }

  public void setExpiresAt(Date expiresAt) {
    if (expiresAt != null) {
      this.expiresAt = new Date(expiresAt.getTime());
    }
  }

  public boolean isUserPresence() {
    return userPresence;
  }

  public void setUserPresence(boolean userPresence) {
    this.userPresence = userPresence;
  }

  public boolean isPrompt() {
    return prompt;
  }

  public void setPrompt(boolean prompt) {
    this.prompt = prompt;
  }

  public boolean isShowIdentityDocument() {
    return showIdentityDocument;
  }

  public void setShowIdentityDocument(boolean showIdentityDocument) {
    this.showIdentityDocument = showIdentityDocument;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getUserIdentifier() {
    return userIdentifier;
  }

  public void setUserIdentifier(String userIdentifier) {
    this.userIdentifier = userIdentifier;
  }

  public String getTrusonaId() {
    return trusonaId;
  }

  public void setTrusonaId(String trusonaId) {
    this.trusonaId = trusonaId;
  }

  public Date getCreatedAt() {
    return createdAt != null ? new Date(createdAt.getTime()) : null;
  }

  public void setCreatedAt(Date createdAt) {
    if (createdAt != null) {
      this.createdAt = new Date(createdAt.getTime());
    }
  }

  public Date getUpdatedAt() {
    return updatedAt != null ? new Date(updatedAt.getTime()) : null;
  }

  public void setUpdatedAt(Date updatedAt) {
    if (updatedAt != null) {
      this.updatedAt = new Date(updatedAt.getTime());
    }
  }

  public TrusonaficationResultResponse getResult() {
    return result;
  }

  public void setResult(TrusonaficationResultResponse result) {
    this.result = result;
  }

  @Override
  public int hashCode() {
    return reflectionHashCode(345749, 2332221, this);
  }
}
