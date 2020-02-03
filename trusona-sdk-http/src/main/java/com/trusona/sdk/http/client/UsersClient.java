package com.trusona.sdk.http.client;

import com.trusona.sdk.http.CallHandler;
import com.trusona.sdk.http.ErrorHandler;
import com.trusona.sdk.http.GenericErrorHandler;
import com.trusona.sdk.http.ServiceGenerator;
import com.trusona.sdk.http.client.v2.service.UserService;
import com.trusona.sdk.resources.UsersApi;
import com.trusona.sdk.resources.exception.TrusonaException;
import com.trusona.sdk.resources.exception.UserNotFoundException;
import retrofit2.Response;

public class UsersClient implements UsersApi {

  private final ServiceGenerator serviceGenerator;
  private final GenericErrorHandler defaultErrorHandler;

  public UsersClient(ServiceGenerator serviceGenerator) {
    this.serviceGenerator = serviceGenerator;
    this.defaultErrorHandler = new GenericErrorHandler();
  }

  @Override
  public Void deactivateUser(String userIdentifier) throws TrusonaException {
    UserService userService = serviceGenerator.getService(UserService.class);

    ErrorHandler notFoundErrorHandler = new BaseErrorHandler() {
      @Override
      public void handleErrors(Response<?> response) throws TrusonaException {
        if (response.code() == 404) {
          throw new UserNotFoundException("The user you are attempting to deactivate does not exist or is already inactive.");
        }
      }
    };

    return new CallHandler<>(userService.deleteUser(userIdentifier))
      .handle(notFoundErrorHandler, defaultErrorHandler);
  }
}