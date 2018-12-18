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
      case AP_PRODUCTION:
        return new ApProdEnvironment();
      case AP_UAT:
        return new ApUatEnvironment();
      default:
        throw new IllegalArgumentException("Unexpected environment: " + env);
    }
  }
}
