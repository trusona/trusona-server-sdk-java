package com.trusona.sdk.resources;

import com.trusona.sdk.resources.exception.TrusonaException;
import com.trusona.sdk.resources.exception.UserNotFoundException;

public interface UsersApi {

  Void deactivateUser(String userIdentifier) throws  TrusonaException;

}