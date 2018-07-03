package com.trusona.sdk.http;

import com.trusona.sdk.config.JacksonConfig;
import com.trusona.sdk.http.client.security.AuthTokenParser;
import com.trusona.sdk.http.client.security.ParsedToken;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;

public class ApiCredentials {
  private static final Logger logger = LoggerFactory.getLogger(AuthTokenParser.class);

  private final String token;
  private final String secret;

  public ApiCredentials(String token, String secret) {
    this.token = token;
    this.secret = secret;
  }

  public String getToken() {
    return token;
  }

  public String getSecret() {
    return secret;
  }

  public ParsedToken getParsedToken() {
    ParsedToken parsedToken = null;

    String[] parts = token.split("\\.");

    if (parts.length == 3) {
      String data = addPadding(parts[1]
        .replaceAll("_", "/")
        .replace("-", "+"));

      byte[] decodedData = Base64.decodeBase64(data);

      try {
        parsedToken = JacksonConfig.getObjectMapper().readValue(new String(decodedData, Charset.forName("UTF-8")), ParsedToken.class);
      }
      catch (IOException e) {
        logger.error("Error parsing token", e);
      }
    }
    return parsedToken;
  }

  private String addPadding(String s) {
    int missing = s.length() % 4;
    String ret = s;

    for (int i = 0; i < missing; i++) {
      ret = ret + "=";
    }

    return ret;
  }
}
