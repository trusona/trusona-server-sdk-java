package com.trusona.sdk.http.client.v2.response;

import com.trusona.sdk.http.client.v2.request.TrusonaficationRequest;

import java.util.Date;
import java.util.UUID;

import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;

/**
 * Copyright Trusona, Inc.
 * Created on 1/22/18 for trusona-server-sdk.
 */
public class TrusonaficationResponse extends TrusonaficationRequest {
  private static final long serialVersionUID = 3555946086952925089L;

  private UUID id;
  private String status;
  private String userIdentifier;
  private String trusonaId;
  private Date createdAt;
  private Date updatedAt;
  private TrusonaficationResultResponse result;

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
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public Date getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Date updatedAt) {
    this.updatedAt = updatedAt;
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
