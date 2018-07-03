package com.trusona.sdk.http.client.security;

import java.io.IOException;

public interface HmacSignatureGenerator {
  String getSignature(HmacMessage message, String secret) throws IOException;
}
