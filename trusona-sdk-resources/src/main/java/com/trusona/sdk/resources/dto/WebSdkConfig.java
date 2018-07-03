package com.trusona.sdk.resources.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.UUID;

public class WebSdkConfig extends BaseDto {
  private static final long serialVersionUID = 7597812506386196289L;

  @JsonProperty("truCodeUrl")
  private String truCodeUrl;

  @JsonProperty("relyingPartyId")
  private UUID relyingPartyId;

  public WebSdkConfig(String truCodeUrl, UUID relyingPartyId) {
    this.truCodeUrl = truCodeUrl;
    this.relyingPartyId = relyingPartyId;
  }

  public String getTruCodeUrl() {
    return truCodeUrl;
  }

  public UUID getRelyingPartyId() {
    return relyingPartyId;
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(39485743, 283745, this);
  }
}
