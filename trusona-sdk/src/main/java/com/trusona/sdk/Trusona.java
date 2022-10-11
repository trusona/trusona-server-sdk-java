package com.trusona.sdk;

import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.trusona.sdk.config.JacksonConfig;
import com.trusona.sdk.http.ApiCredentials;
import com.trusona.sdk.http.CallHandler;
import com.trusona.sdk.http.ErrorHandler;
import com.trusona.sdk.http.GenericErrorHandler;
import com.trusona.sdk.http.ServiceGenerator;
import com.trusona.sdk.http.client.DevicesClient;
import com.trusona.sdk.http.client.TrusonaficationClient;
import com.trusona.sdk.http.client.UsersClient;
import com.trusona.sdk.http.client.security.DefaultHmacSignatureGenerator;
import com.trusona.sdk.http.client.security.ParsedToken;
import com.trusona.sdk.http.client.v2.service.IdentityDocumentService;
import com.trusona.sdk.http.client.v2.service.TruCodeService;
import com.trusona.sdk.http.client.v2.service.UserDeviceService;
import com.trusona.sdk.http.environment.CustomEnvironment;
import com.trusona.sdk.resources.DevicesApi;
import com.trusona.sdk.resources.TrusonaApi;
import com.trusona.sdk.resources.TrusonaficationApi;
import com.trusona.sdk.resources.UsersApi;
import com.trusona.sdk.resources.dto.Device;
import com.trusona.sdk.resources.dto.IdentityDocument;
import com.trusona.sdk.resources.dto.TruCode;
import com.trusona.sdk.resources.dto.Trusonafication;
import com.trusona.sdk.resources.dto.TrusonaficationResult;
import com.trusona.sdk.resources.dto.TrusonaficationStatus;
import com.trusona.sdk.resources.dto.UserDevice;
import com.trusona.sdk.resources.dto.WebSdkConfig;
import com.trusona.sdk.resources.exception.DeviceAlreadyBoundException;
import com.trusona.sdk.resources.exception.DeviceNotFoundException;
import com.trusona.sdk.resources.exception.NoIdentityDocumentsException;
import com.trusona.sdk.resources.exception.TrusonaException;
import com.trusona.sdk.resources.exception.ValidationException;
import org.apache.commons.lang3.ThreadUtils;
import org.apache.commons.lang3.time.StopWatch;

import static org.apache.commons.lang3.Validate.notEmpty;

/**
 * The main class to interact with Trusona.
 */
public class Trusona implements TrusonaApi {

  private final ServiceGenerator serviceGenerator;
  private final ApiCredentials apiCredentials;
  private final ErrorHandler genericErrorHandler;
  private final TrusonaficationApi trusonaficationApi;
  private final DevicesApi devicesApi;
  private final UsersApi usersApi;

  Duration pollingInterval = Duration.ofSeconds(5);


  Trusona(ServiceGenerator serviceGenerator, ApiCredentials apiCredentials,
          TrusonaficationApi trusonaficationApi, DevicesApi devicesApi, UsersApi usersApi) {

    this.serviceGenerator = serviceGenerator;
    this.apiCredentials = apiCredentials;
    this.trusonaficationApi = trusonaficationApi;
    this.devicesApi = devicesApi;
    this.usersApi = usersApi;

    this.genericErrorHandler = new GenericErrorHandler();
  }

  //TODO: Remove these intermediary constructors after everything is delegated to clients.
  Trusona(ServiceGenerator sg, ApiCredentials apiCredentials) {
    this(sg, apiCredentials, new TrusonaficationClient(sg), new DevicesClient(sg),
      new UsersClient(sg));
  }

  Trusona(TrusonaEnvironment environment, ApiCredentials apiCredentials) {
    this(ServiceGenerator.create(
        EnvironmentFactory.getEnvironment(environment),
        apiCredentials,
        new DefaultHmacSignatureGenerator()),
      apiCredentials);
  }

  Trusona(CustomEnvironment environment, ApiCredentials apiCredentials) {
    this(ServiceGenerator.create(
        environment,
        apiCredentials,
        new DefaultHmacSignatureGenerator()),
      apiCredentials);
  }

  /**
   * Create a Trusona object using an endpoint URL
   *
   * @param token    Your API token, provided by Trusona
   * @param secret   Your API secret, provided by Trusona
   * @param endpoint The Trusona endpoint URL you would like to point at
   */
  public Trusona(String token, String secret, URL endpoint) {
    this(EnvironmentFactory.getCustomEnvironment(endpoint), new ApiCredentials(token, secret));
  }

  /**
   * Create a Trusona object using a TrusonaEnvironment
   *
   * @param token       Your API token, provided by Trusona
   * @param secret      Your API secret, provided by Trusona
   * @param environment The environment you would like to point at
   */
  public Trusona(String token, String secret, TrusonaEnvironment environment) {
    this(environment, new ApiCredentials(token, secret));
  }

  /**
   * Create a Trusona object
   *
   * @param token  Your API token, provided by Trusona
   * @param secret Your API secret, provided by Trusona
   */
  public Trusona(String token, String secret) {
    this(token, secret, TrusonaEnvironment.PRODUCTION);
  }


  private UserDeviceService getUserDeviceService() {
    return serviceGenerator.getService(UserDeviceService.class);
  }

  private TruCodeService getTruCodeService() {
    return serviceGenerator.getService(TruCodeService.class);
  }

  private IdentityDocumentService getIdentityDocumentService() {
    return serviceGenerator.getService(IdentityDocumentService.class);
  }

  /**
   * Create a user device binding in Trusona between a User and a Device, referenced by their
   * identifiers. After creation, the binding will be inactive, and must be explicitly activated
   * before the User can use the Device to complete Trusonafications.
   *
   * @param userIdentifier   A unique identifier for the user. This will be how Trusona references
   *                         your users, and should not change.
   * @param deviceIdentifier A unique identifier for the device. This identifier is generated by the
   *                         Trusona mobile SDKs
   * @return a {@link UserDevice} object that represents the current state of the user device
   * binding.
   * @throws DeviceNotFoundException     If the deviceIdentifier hasn't been registered with
   *                                     Trusona.
   * @throws DeviceAlreadyBoundException If the device is already bound to a different user.
   * @throws ValidationException         If the userIdentifier or deviceIdentifier params are
   *                                     blank.
   * @throws TrusonaException            If there was a problem processing the API request.
   */
  @Override
  public UserDevice createUserDevice(String userIdentifier, String deviceIdentifier)
    throws DeviceNotFoundException, DeviceAlreadyBoundException, ValidationException, TrusonaException {
    UserDevice request = new UserDevice();
    request.setDeviceIdentifier(deviceIdentifier);
    request.setUserIdentifier(userIdentifier);

    ErrorHandler createUserDeviceErrorHandler = response -> {
      switch (response.code()) {
        case 409:
          throw new DeviceAlreadyBoundException(
            "A different user has already been bound to this device.");
        case 424:
          throw new DeviceNotFoundException(
            "The device you are attempting to bind to a user does not exist. " +
              "The device will need to be re-registered with Trusona before attempting to bind it again.");
      }
    };

    return new CallHandler<>(getUserDeviceService().createUserDevice(request))
      .handle(createUserDeviceErrorHandler, genericErrorHandler);
  }

  /**
   * Activates a user device binding in Trusona. After a binding is active, a user can respond to
   * Trusonafications. Only call this method after you have verified the identity of the user.
   *
   * @param activationCode The activation code from {@link Trusona#createUserDevice(String,
   *                       String)}
   * @return true if the device is activated
   * @throws DeviceNotFoundException If the activationCode provided couldn't be matched up with a
   *                                 device.
   * @throws ValidationException     If the activationCode is blank.
   * @throws TrusonaException        If there was a problem processing the API request.
   */
  @Override
  public boolean activateUserDevice(String activationCode)
    throws DeviceNotFoundException, ValidationException, TrusonaException {
    UserDevice request = new UserDevice();
    request.setActive(true);

    ErrorHandler activeErrorHandler = response -> {
      if (response.code() == 404) {
        throw new DeviceNotFoundException(
          "The device you are attempting to activate does not exist. " +
            "You will need to re-register the device and re-bind it to the user to get a new activation code.");
      }
    };

    return new CallHandler<>(getUserDeviceService().updateUserDevice(activationCode, request))
      .handle(activeErrorHandler, genericErrorHandler).isActive();
  }

  /**
   * Creates a Trusonafication and returns a TrusonaficationResult with the current status of the
   * Trusonafication. See {@link TrusonaficationStatus} for the possible statuses.
   *
   * @param trusonafication The Trusonafication to create
   * @return A TrusonaficationResult describing the current status of the Trusonafication
   * @throws NoIdentityDocumentsException If an executive Trusonafication was requested for a user
   *                                      with no identity documents
   * @throws TrusonaException             If there was a problem processing the API request.
   */
  @Override
  public TrusonaficationResult createTrusonafication(Trusonafication trusonafication)
    throws TrusonaException {
    return trusonaficationApi.createTrusonafication(trusonafication);
  }

  /**
   * Gets a TrusonaficationResult for a given Trusonafication ID. This will block until the
   * Trusonafication is no longer IN_PROGRESS
   *
   * @param trusonaficationId The ID of the trusonafication if it exists, or null
   * @return A TrusonaficationResult for the given Trusonafication.
   * @throws TrusonaException If there was a problem processing the API request, or polling was
   *                          interrupted.
   */
  @Override
  public TrusonaficationResult getTrusonaficationResult(UUID trusonaficationId)
    throws TrusonaException {
    return trusonaficationApi.getTrusonaficationResult(trusonaficationId);
  }

  @Override
  public Void cancelTrusonafication(UUID trusonaficationId) throws TrusonaException {
    return trusonaficationApi.cancelTrusonafication(trusonaficationId);
  }

  /**
   * Get the configuration for the Web SDK. Include this in your Javascript code for rendering
   * Trucodes.
   *
   * @return The Web SDK config serialized to JSON
   * @throws TrusonaException If the provided access token is invalid
   */
  @Override
  public String getWebSdkConfig() throws TrusonaException {
    ParsedToken parsedToken = apiCredentials.getParsedToken();
    if (parsedToken == null) {
      throw new TrusonaException(
        "The provided access token is invalid. Please check your configuration");
    }

    WebSdkConfig config = new WebSdkConfig(serviceGenerator.getBaseUrl(), parsedToken.getSubject());
    try {
      return JacksonConfig.getObjectMapper().writeValueAsString(config);
    } catch (JsonProcessingException e) {
      throw new TrusonaException(
        "Could not serializer Web SDK config. Contact Trusona to determine the exact cause", e);
    }
  }

  /**
   * Get a TruCode that has been paired.
   *
   * @param id The ID of the TruCode
   * @return the paired TruCode if it exists, or null
   * @throws TrusonaException If any network or server errors occur.
   */
  @Override
  public TruCode getPairedTruCode(UUID id) throws TrusonaException {
    return new CallHandler<>(getTruCodeService().getPairedTrucode(id))
      .handle(genericErrorHandler);
  }

  /**
   * Continuously look for a TruCode that has been paired.
   *
   * @param id      The ID of the TruCode
   * @param timeout The amount of time in milliseconds to look for the TruCode
   * @return the paired TruCode if it exists, or null
   * @throws TrusonaException If any network or server errors occur.
   */
  @Override
  public TruCode getPairedTruCode(UUID id, Long timeout) throws TrusonaException {
    long stopTime = System.currentTimeMillis() + timeout;
    StopWatch stopWatch = StopWatch.createStarted();

    while (stopWatch.getTime() < stopTime) {
      TruCode truCode = getPairedTruCode(id);

      if (truCode != null) {
        return truCode;
      } else {
        long remainingTime = stopTime - System.currentTimeMillis();

        if (remainingTime > 0) {
          long sleepTime = Math.min(pollingInterval.toMillis(), remainingTime);

          try {
            ThreadUtils.sleep(Duration.ofMillis(sleepTime));
          } catch (InterruptedException e) {
            throw new TrusonaException("Thread was interrupted while polling for a paired TruCode", e);
          }
        }
      }
    }

    return null;
  }

  @Override
  public List<IdentityDocument> findIdentityDocuments(String userIdentifier) throws TrusonaException {
    return new CallHandler<>(getIdentityDocumentService().findIdentityDocuments(userIdentifier))
      .handle(genericErrorHandler);
  }

  @Override
  public IdentityDocument getIdentityDocument(UUID id) throws TrusonaException {
    return new CallHandler<>(getIdentityDocumentService().getIdentityDocument(id)).handle(genericErrorHandler);
  }

  /**
   * Deactivate a user by their user identifier
   *
   * @param userIdentifier A String that would be used to identify the user.
   * @return Void
   * @throws TrusonaException If any network or server errors occur.
   */
  @Override
  public Void deactivateUser(String userIdentifier) throws TrusonaException {
    return usersApi.deactivateUser(notEmpty(userIdentifier));
  }

  /**
   * Finds the specified device.
   *
   * @param deviceIdentifier The identifier of the device
   * @return the device if found, otherwise null
   * @throws TrusonaException If any network or server errors occur.
   */
  @Override
  public Device getDevice(String deviceIdentifier) throws TrusonaException {
    return devicesApi.getDevice(deviceIdentifier);
  }
}
