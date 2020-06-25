package com.trusona.sdk;

import com.trusona.sdk.http.environment.*;

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

  public static Environment getCustomEnvironment(String endpointUrl) {
    if (endpointUrl == null) {
      throw new IllegalArgumentException("endpointUrl must not be null");
    }
    return new CustomEnvironment(endpointUrl);
  }
}
