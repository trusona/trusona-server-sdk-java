package com.trusona.sdk.resources.dto;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Date;
import java.util.UUID;

public class IdentityDocument extends BaseDto {

  private static final long serialVersionUID = -1940208243248465229L;

  private UUID id;
  private String hash;
  private VerificationStatus verificationStatus;
  private Date verifiedAt;
  private String type;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getHash() {
    return hash;
  }

  public void setHash(String hash) {
    this.hash = hash;
  }

  public VerificationStatus getVerificationStatus() {
    return verificationStatus;
  }

  public void setVerificationStatus(VerificationStatus verificationStatus) {
    this.verificationStatus = verificationStatus;
  }

  public Date getVerifiedAt() {
    return verifiedAt != null ? new Date(verifiedAt.getTime()) : null;
  }

  public void setVerifiedAt(Date verifiedAt) {
    if (verifiedAt != null) {
      this.verifiedAt = new Date(verifiedAt.getTime());
    }
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(23545, 98667, this);
  }
}