package com.trusona.sdk.http.client.v2;

import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;

import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public abstract class BaseResponse implements Serializable {
  private static final long serialVersionUID = -789268235971L;

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
