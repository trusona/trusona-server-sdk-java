package com.trusona.sdk.http.client;

import com.trusona.sdk.http.CallHandler;
import com.trusona.sdk.http.GenericErrorHandler;
import com.trusona.sdk.http.ServiceGenerator;
import com.trusona.sdk.http.client.v2.service.UserService;
import com.trusona.sdk.resources.UsersApi;
import com.trusona.sdk.resources.exception.TrusonaException;

public class UsersClient implements UsersApi {

  private final ServiceGenerator serviceGenerator;
  private final GenericErrorHandler defaultErrorHandler;

  public UsersClient(ServiceGenerator serviceGenerator) {
    this.serviceGenerator = serviceGenerator;
    this.defaultErrorHandler = new GenericErrorHandler();
  }

  @Override
  public Void deleteUser(String userIdentifier) throws TrusonaException {
    UserService userService = serviceGenerator.getService(UserService.class);
    return new CallHandler<>(userService.deleteUser(userIdentifier)).handle(defaultErrorHandler);
  }
}