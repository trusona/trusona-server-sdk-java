package com.trusona.sdk.config;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.PropertyNamingStrategy.SNAKE_CASE;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import java.text.DateFormat;

/**
 * Copyright Trusona, Inc. Created on 1/19/18 for trusona-server-sdk.
 */
public class JacksonConfig {

  static DateFormat getDateFormat() {
    // todo: resolve this deprecation
    return new ISO8601DateFormat();
  }

  public static ObjectMapper getObjectMapper() {
    return new ObjectMapper()
      .setPropertyNamingStrategy(SNAKE_CASE)
      .setSerializationInclusion(JsonInclude.Include.NON_NULL)
      .setDateFormat(getDateFormat())
      .disable(FAIL_ON_UNKNOWN_PROPERTIES)
      .disable(WRITE_DATES_AS_TIMESTAMPS);
  }
}