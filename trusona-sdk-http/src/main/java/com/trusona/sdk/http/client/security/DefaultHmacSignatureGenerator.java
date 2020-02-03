package com.trusona.sdk.http.client.security;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang3.StringUtils.LF;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.commons.lang3.StringUtils;

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
    return Base64.encodeBase64String(new HmacUtils(HmacAlgorithms.HMAC_SHA_256, secret)
                                       .hmacHex(valueToDigest)
                                       .getBytes(UTF_8));
  }
}