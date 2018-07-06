package com.trusona.sdk.http.client.interceptor;

import com.trusona.sdk.http.ApiCredentials;
import com.trusona.sdk.http.Headers;
import com.trusona.sdk.http.client.security.HmacSignatureGenerator;
import com.trusona.sdk.http.client.security.RequestHmacMessage;
import com.trusona.sdk.http.client.security.ResponseHmacMessage;
import com.trusona.sdk.http.client.security.TrusonaAuthorizationHeader;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HmacAuthInterceptor implements Interceptor {
  private static final Logger logger = LoggerFactory.getLogger(HmacAuthInterceptor.class);
  private static final String EMPTY_JSON = "{}";
  private static final String EMPTY_JSON_CONTENT_TYPE = "application/json; charset=utf-8";

  private final HmacSignatureGenerator signatureGenerator;
  private final ApiCredentials apiCredentials;

  public HmacAuthInterceptor(HmacSignatureGenerator signatureGenerator, ApiCredentials apiCredentials) {
    this.signatureGenerator = signatureGenerator;
    this.apiCredentials = apiCredentials;
  }

  public HmacAuthInterceptor(HmacSignatureGenerator signatureGenerator, String accessToken, String macSecret) {
    this(signatureGenerator, new ApiCredentials(accessToken, macSecret));
  }

  @Override
  public Response intercept(Chain chain) throws IOException {
    Request request = chain.request();

    RequestHmacMessage hmacMessage = new RequestHmacMessage(request);
    String signature = signatureGenerator.getSignature(hmacMessage, apiCredentials.getSecret());
    TrusonaAuthorizationHeader authorizationHeader = new TrusonaAuthorizationHeader(apiCredentials.getToken(), signature);

    return validateResponse(chain.proceed(request.newBuilder()
      .header(com.trusona.sdk.http.Headers.AUTHORIZATION, authorizationHeader.toString())
      .build()
    ));
  }

  private ResponseBody getEmptyJson() {
    return ResponseBody.create(MediaType.parse(EMPTY_JSON_CONTENT_TYPE), EMPTY_JSON);
  }

  private Response failResponse(Response incomingResponse) {
    logger.warn("Response signature failed validation");
    return incomingResponse.newBuilder()
      .body(getEmptyJson())
      .code(401)
      .build();
  }

  private Response validateResponse(Response incomingResponse) throws IOException {
    if (!incomingResponse.isSuccessful()) {
      return incomingResponse;
    }

    String signature = incomingResponse.header(Headers.X_SIGNATURE);

    if (signature != null) {
      String expectedSignature = signatureGenerator.getSignature(new ResponseHmacMessage(incomingResponse), apiCredentials.getSecret());

      if (signature.equals(expectedSignature)) {
        return incomingResponse;
      }
      else {
        return failResponse(incomingResponse);
      }
    }
    else {
      return failResponse(incomingResponse);
    }
  }
}
