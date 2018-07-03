package com.trusona.sdk.resources.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;

public abstract class BaseDto implements Serializable {
  private static final long serialVersionUID = -5864001556303138199L;

  @Override
  public boolean equals(Object object) {
    return object != null
      && getClass().equals(object.getClass())
      && EqualsBuilder.reflectionEquals(this, object);
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, SHORT_PREFIX_STYLE);
  }

  @Override
  public abstract int hashCode();
}
