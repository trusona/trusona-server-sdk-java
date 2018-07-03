package com.trusona.sdk.resources.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Date;

public class Device extends BaseDto {
  private static final long serialVersionUID = 2973513094326789650L;

  private Date activatedAt;

  @JsonProperty("is_active")
  private boolean active;

  public Date getActivatedAt() {
    return activatedAt;
  }

  public void setActivatedAt(Date activatedAt) {
    this.activatedAt = activatedAt;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(21437, 430161, this);
  }
}
