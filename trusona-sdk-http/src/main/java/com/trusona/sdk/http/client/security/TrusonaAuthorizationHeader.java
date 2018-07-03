package com.trusona.sdk.http.client.security;

import org.apache.commons.lang3.StringUtils;

public class TrusonaAuthorizationHeader {
  private static final char DELIMITER = ':';

  private static final String TOKEN_TYPE = "TRUSONA";

  private final String token;
  private final String signature;

  public TrusonaAuthorizationHeader() {
    this(null, null);
  }

  public TrusonaAuthorizationHeader(String token, String signature) {
    this.token      = token;
    this.signature  = signature;
  }

  public static TrusonaAuthorizationHeader parseAuthorizationHeader(String rawHeaderValue) {
    String headerValue = StringUtils.trimToNull(rawHeaderValue);

    if(headerValue != null) {
      String[] headerParts = StringUtils.split(headerValue);

      if(headerParts.length == 2) {
        String[] values = StringUtils.split(headerParts[1], DELIMITER);

        if(values.length == 2) {
          return new TrusonaAuthorizationHeader(values[0], values[1]);
        }
      }
    }

    return new TrusonaAuthorizationHeader();
  }

  public String getToken() {
    return token;
  }

  public String getSignature() {
    return signature;
  }

  public boolean isValid() {
    return assertNotBlank(getToken(), getSignature());
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder(String.format("%s ", TOKEN_TYPE));
    builder.append(getToken());

    if(getSignature() != null) {
      builder.append(DELIMITER).append(getSignature());
    }
    return builder.toString();
  }

  private boolean assertNotBlank(String... values) {
    for (String v : values) {
      if (StringUtils.isBlank(v)) {
        return false;
      }
    }
    return true;
  }
}