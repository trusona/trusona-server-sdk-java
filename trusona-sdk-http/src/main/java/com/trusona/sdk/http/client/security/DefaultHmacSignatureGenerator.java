package com.trusona.sdk.http.client.security;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang3.StringUtils.LF;

public class DefaultHmacSignatureGenerator implements HmacSignatureGenerator {

  public String getSignature(HmacMessage hmacMessage, String secret) throws IOException {

    List<String> parts = Arrays.asList(
      hmacMessage.getMethod(),
      hmacMessage.getBodyDigest(),
      hmacMessage.getContentType(),
      hmacMessage.getDate(),
      hmacMessage.getRequestUri()
    );

    String valueToDigest = StringUtils.join(parts, LF);

    return Base64.encodeBase64String(HmacUtils.hmacSha256Hex(secret, valueToDigest).getBytes(UTF_8));
  }
}