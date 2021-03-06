package com.trusona.sdk.http.client.v2.response;

import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.trusona.sdk.resources.dto.BaseDto;
import java.util.UUID;

/**
 * Copyright Trusona, Inc.
 * Created on 1/22/18 for trusona-server-sdk.
 */
public class TrusonaficationResultResponse extends BaseDto {
  private static final long serialVersionUID = 5349104976805324511L;

  private UUID id;
  @JsonProperty("is_accepted")
  private boolean accepted;
  private Integer acceptedLevel;
  private String boundUserIdentifier;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public boolean isAccepted() {
    return accepted;
  }

  public void setAccepted(boolean accepted) {
    this.accepted = accepted;
  }

  public Integer getAcceptedLevel() {
    return acceptedLevel;
  }

  public void setAcceptedLevel(Integer acceptedLevel) {
    this.acceptedLevel = acceptedLevel;
  }

  public String getBoundUserIdentifier() {
    return boundUserIdentifier;
  }

  public void setBoundUserIdentifier(String boundUserIdentifier) {
    this.boundUserIdentifier = boundUserIdentifier;
  }

  @Override
  public int hashCode() {
    return reflectionHashCode(3929909, 288273783, this);
  }
}
