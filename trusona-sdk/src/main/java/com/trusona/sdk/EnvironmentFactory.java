package com.trusona.sdk;

import com.trusona.sdk.http.environment.*;
import java.net.URL;

public class EnvironmentFactory {

  public static Environment getEnvironment(TrusonaEnvironment env) {
    if (env == null) {
      throw new IllegalArgumentException("environment must not be null");
    }
    switch (env) {
      case PRODUCTION:
        return new ProdEnvironment();
      case UAT:
        return new UatEnvironment();
      case AP_PRODUCTION:
        return new ApProdEnvironment();
      case AP_UAT:
        return new ApUatEnvironment();
      case EU_PRODUCTION:
        return new EuProdEnvironment();
      case EU_UAT:
        return new EuUatEnvironment();
      case TEST_VERIFY:
        return new TestVerifyEnvironment();
      default:
        throw new IllegalArgumentException("Unexpected environment: " + env);
    }
  }

  public static CustomEnvironment getCustomEnvironment(URL endpoint) {
    if (endpoint == null) {
      throw new IllegalArgumentException("endpoint must not be null");
    }
    return new CustomEnvironment(endpoint);
  }
}
