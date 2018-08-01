package com.trusona.sdk.resources;

import com.trusona.sdk.resources.dto.Device;
import com.trusona.sdk.resources.dto.IdentityDocument;
import com.trusona.sdk.resources.dto.TruCode;
import com.trusona.sdk.resources.dto.UserDevice;
import com.trusona.sdk.resources.exception.DeviceAlreadyBoundException;
import com.trusona.sdk.resources.exception.DeviceNotFoundException;
import com.trusona.sdk.resources.exception.TrusonaException;
import com.trusona.sdk.resources.exception.ValidationException;

import java.util.List;
import java.util.UUID;

public interface TrusonaApi extends TrusonaficationApi, DevicesApi {

  UserDevice createUserDevice(String userIdentifier, String deviceIdentifier)
    throws DeviceNotFoundException, DeviceAlreadyBoundException, ValidationException, TrusonaException;

  boolean activateUserDevice(String activationCode)
    throws DeviceNotFoundException, ValidationException, TrusonaException;


  String getWebSdkConfig() throws TrusonaException;

  TruCode getPairedTruCode(UUID id) throws TrusonaException;

  TruCode getPairedTruCode(UUID id, Long timeout) throws TrusonaException;

  List<IdentityDocument> findIdentityDocuments(String userIdentifier) throws TrusonaException;

  IdentityDocument getIdentityDocument(UUID id) throws TrusonaException;
}