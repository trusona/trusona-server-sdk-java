package com.trusona.sdk.http.client;

import static com.trusona.sdk.resources.dto.TrusonaficationStatus.IN_PROGRESS;

import com.trusona.sdk.http.CallHandler;
import com.trusona.sdk.http.ErrorHandler;
import com.trusona.sdk.http.GenericErrorHandler;
import com.trusona.sdk.http.ServiceGenerator;
import com.trusona.sdk.http.client.v2.response.ErrorResponse;
import com.trusona.sdk.http.client.v2.response.TrusonaficationResponse;
import com.trusona.sdk.http.client.v2.service.TrusonaficationService;
import com.trusona.sdk.resources.TrusonaficationApi;
import com.trusona.sdk.resources.dto.AuthenticatorType;
import com.trusona.sdk.resources.dto.Trusonafication;
import com.trusona.sdk.resources.dto.TrusonaficationResult;
import com.trusona.sdk.resources.dto.TrusonaficationStatus;
import com.trusona.sdk.resources.exception.InvalidCredentialsException;
import com.trusona.sdk.resources.exception.NoIdentityDocumentsException;
import com.trusona.sdk.resources.exception.ResourceNotFound;
import com.trusona.sdk.resources.exception.ResourceNotProcessable;
import com.trusona.sdk.resources.exception.TrusonaException;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import retrofit2.Response;

public class TrusonaficationClient implements TrusonaficationApi {

  private final static String LEGACY_PREFIX = "trusonaId:";

  private final ServiceGenerator serviceGenerator;
  private final ErrorHandler defaultErrorHandler;
  private final Duration pollingInterval;


  public TrusonaficationClient(ServiceGenerator serviceGenerator) {
    this(serviceGenerator, new GenericErrorHandler(), Duration.ofSeconds(5));
  }

  TrusonaficationClient(ServiceGenerator serviceGenerator, ErrorHandler errorHandler, Duration pollingInterval) {
    this.serviceGenerator = serviceGenerator;
    this.defaultErrorHandler = errorHandler;
    this.pollingInterval = pollingInterval;
  }

  @Override
  public TrusonaficationResult createTrusonafication(Trusonafication trusonafication) throws TrusonaException {
    TrusonaficationService service = serviceGenerator.getService(TrusonaficationService.class);

    ErrorHandler cannotBeCompletedErrorHandler = new BaseErrorHandler() {
      public void handleErrors(Response<?> response) throws TrusonaException {
        if (response.code() == 424) {
          ErrorResponse errorResponse = getErrorResponse(response);

          if (errorResponse.getError().equals("NO_DOCUMENTS")) {
            throw new NoIdentityDocumentsException(errorResponse.getDescription());
          }
          else {
            throw new TrusonaException(errorResponse.getMessage());
          }
        }
        else if (response.code() == 422) {
          throw new TrusonaException(getErrorResponse(response).getMessage());
        }
      }
    };

    TrusonaficationResponse responseBody = new CallHandler<>(service.createTrusonafication(trusonafication))
      .handle(cannotBeCompletedErrorHandler, defaultErrorHandler);

    return trusonaficationResultFromResponse(responseBody);
  }

  @Override
  public TrusonaficationResult getTrusonaficationResult(UUID trusonaficationId) throws TrusonaException {
    return pollTrusonaficationResult(trusonaficationId, Duration.ZERO);
  }

  @Override
  public TrusonaficationResult pollTrusonaficationResult(UUID trusonaficationId, Duration timeout)
    throws TrusonaException {
    TrusonaficationService service = serviceGenerator.getService(TrusonaficationService.class);

    Instant start = Instant.now();
    TrusonaficationResponse response = null;

    do {
        response = new CallHandler<>(service.getTrusonafication(trusonaficationId))
            .handle(defaultErrorHandler);

        if (response == null || !IN_PROGRESS.equals(TrusonaficationStatus.valueOf(response.getStatus()))) {
            return response == null ? null : trusonaficationResultFromResponse(response);
        }

        if (!timeout.equals(Duration.ZERO) && start.plusMillis(timeout.toMillis()).isBefore(Instant.now())) {
            break;
        }

        sleep(pollingInterval);

    } while (IN_PROGRESS.equals(TrusonaficationStatus.valueOf(response.getStatus())));

    return null;
  }

  @Override
  public Void cancelTrusonafication(UUID trusonaficationId) throws TrusonaException {
    TrusonaficationService service = serviceGenerator.getService(TrusonaficationService.class);

    return new CallHandler<>(service.cancelTrusonafication(trusonaficationId))
      .handle(response -> {
        if (response.code() == 403) {
          throw new InvalidCredentialsException();
        }
        if (response.code() == 404) {
          throw new ResourceNotFound("Trusonafication was not found");
        }
        if (response.code() == 422) {
          throw new ResourceNotProcessable("Trusonafication could not be cancelled");
        }

      }, defaultErrorHandler);
  }

  static TrusonaficationResult trusonaficationResultFromResponse(TrusonaficationResponse response) {
    String userIdentifier;

    if (response.getTrusonaId() != null) {
      userIdentifier = LEGACY_PREFIX + response.getTrusonaId();
    }
    else {
      userIdentifier = response.getUserIdentifier();
    }

    AuthenticatorType authenticatorType = Optional
      .ofNullable(response.getAuthenticatorType())
      .map(AuthenticatorType::valueOf)
      .orElse(null);

    return new TrusonaficationResult(
      response.getId(),
      TrusonaficationStatus.valueOf(response.getStatus()),
      userIdentifier,
      response.getExpiresAt(),
      response.getResult() != null ? response.getResult().getBoundUserIdentifier() : null,
      response.getCreatedAt(),
      response.getUpdatedAt(),
      authenticatorType,
      response.getMagicLinkEmail()
    );
  }

  private static void sleep(Duration duration) throws TrusonaException {
    try {
      Thread.sleep(duration.toMillis());
    }
    catch (InterruptedException e) {
      throw new TrusonaException("Thread was interrupted while polling for trusonafication result", e);
    }
  }
}
