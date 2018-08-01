package com.trusona.sdk.resources;

import com.trusona.sdk.resources.dto.Device;
import com.trusona.sdk.resources.exception.TrusonaException;

public interface DevicesApi {
  Device getDevice(String deviceIdentifer) throws TrusonaException;
}