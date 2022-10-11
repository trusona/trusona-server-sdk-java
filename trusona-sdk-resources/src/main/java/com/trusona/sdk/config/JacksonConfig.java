package com.trusona.sdk.config;

import java.text.DateFormat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.trusona.sdk.annotation.SuppressFBWarnings;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SNAKE_CASE;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

/**
 * Copyright Trusona, Inc. Created on 1/19/18 for trusona-server-sdk.
 */
public class JacksonConfig {

  private static final DateFormat DATE_FORMAT = new ISO8601DateFormat();

  @SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"}, justification = "Instance is not accessed by untrusted code")
  public static DateFormat getDateFormat() {
    return DATE_FORMAT;
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