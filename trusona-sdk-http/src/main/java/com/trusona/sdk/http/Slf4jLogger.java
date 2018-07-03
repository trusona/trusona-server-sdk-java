package com.trusona.sdk.http;

import okhttp3.logging.HttpLoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Slf4jLogger implements HttpLoggingInterceptor.Logger {
  private static final Logger logger = LoggerFactory.getLogger("TrusonaClientHttp");

  @Override
  public void log(String message) {
    logger.info(message);
  }
}
