package com.trusona.sdk.http.client.v2.request;

import com.trusona.sdk.http.client.v2.BaseRequestResponse;

import java.util.Date;

import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;

/**
 * Copyright Trusona, Inc.
 * Created on 1/22/18 for trusona-server-sdk.
 */
public class TrusonaficationRequest extends BaseRequestResponse {
  private static final long serialVersionUID = -250413453219401260L;

  private String  deviceIdentifier;
  private String  userIdentifier;
  private int     desiredLevel;
  private String  action;
  private String  resource;
  private Date    expiresAt;
  private String  callbackUrl;
  private boolean userPresence;
  private boolean prompt;
  private boolean showIdentityDocument;

  public TrusonaficationRequest() {
    userPresence = true;
    prompt = true;
  }

  public String getDeviceIdentifier() {
    return deviceIdentifier;
  }

  public void setDeviceIdentifier(String deviceIdentifier) {
    this.deviceIdentifier = deviceIdentifier;
  }

  public String getUserIdentifier() {
    return userIdentifier;
  }

  public void setUserIdentifier(String userIdentifier) {
    this.userIdentifier = userIdentifier;
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
    return expiresAt;
  }

  public void setExpiresAt(Date expiresAt) {
    this.expiresAt = expiresAt;
  }

  public String getCallbackUrl() {
    return callbackUrl;
  }

  public void setCallbackUrl(String callbackUrl) {
    this.callbackUrl = callbackUrl;
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

  @Override
  public int hashCode() {
    return reflectionHashCode(392009, 28138821, this);
  }
}
