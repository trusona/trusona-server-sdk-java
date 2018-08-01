package com.trusona.sdk.resources;

import com.trusona.sdk.resources.exception.TrusonaException;

public interface UsersApi {

  Void deleteUser(String userIdentifier) throws TrusonaException;

}