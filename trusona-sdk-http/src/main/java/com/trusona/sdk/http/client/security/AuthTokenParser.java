package com.trusona.sdk.http.client.security;

public interface AuthTokenParser {
  ParsedToken parseToken(String token);
}
