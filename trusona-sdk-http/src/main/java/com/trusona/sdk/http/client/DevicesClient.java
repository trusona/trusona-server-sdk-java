package com.trusona.sdk.http.client;

import com.trusona.sdk.http.CallHandler;
import com.trusona.sdk.http.ErrorHandler;
import com.trusona.sdk.http.GenericErrorHandler;
import com.trusona.sdk.http.ServiceGenerator;
import com.trusona.sdk.http.client.v2.service.DeviceService;
import com.trusona.sdk.resources.DevicesApi;
import com.trusona.sdk.resources.dto.Device;
import com.trusona.sdk.resources.exception.TrusonaException;

public class DevicesClient  implements DevicesApi {
  private ServiceGenerator serviceGenerator;
  private ErrorHandler defaultErrorHandler;

  public DevicesClient(ServiceGenerator serviceGenerator) {
    this.serviceGenerator = serviceGenerator;
    this.defaultErrorHandler = new GenericErrorHandler();
  }

  @Override
  public Device getDevice(String deviceIdentifer) throws TrusonaException {
    DeviceService deviceService = serviceGenerator.getService(DeviceService.class);

    return new CallHandler<>(deviceService.getDevice(deviceIdentifer))
        .handle(defaultErrorHandler);
  }
}
