package com.trusona.sdk.http.client.security;

import com.trusona.sdk.config.JacksonConfig;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class DefaultAuthTokenParser implements AuthTokenParser {

  private static final Logger logger = LoggerFactory.getLogger(AuthTokenParser.class);

  @Override
  public ParsedToken parseToken(String token) {
    String[] parts = token.split("\\.");

    if (parts.length == 3) {
      String data = addPadding(parts[1].replaceAll("_", "/").replace("-", "+"));

      byte[] decodedData = Base64.decodeBase64(data);

      try {
        return JacksonConfig.getObjectMapper()
          .readValue(new String(decodedData, StandardCharsets.UTF_8), ParsedToken.class);
      }
      catch (IOException e) {
        logger.error("Error parsing token", e);
      }
    }
    return null;
  }

  private String addPadding(String s) {
    int missing = s.length() % 4;
    StringBuilder builder = new StringBuilder(s);

    for (int i = 0; i < missing; i++) {
      builder.append("=");
    }

    return builder.toString();
  }
}