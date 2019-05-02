package com.trusona.sdk.http.client;

import com.trusona.sdk.http.CallHandler;
import com.trusona.sdk.http.ErrorHandler;
import com.trusona.sdk.http.GenericErrorHandler;
import com.trusona.sdk.http.ServiceGenerator;
import com.trusona.sdk.http.client.v2.response.ErrorResponse;
import com.trusona.sdk.http.client.v2.response.TrusonaficationResponse;
import com.trusona.sdk.http.client.v2.service.TrusonaficationService;
import com.trusona.sdk.resources.TrusonaficationApi;
import com.trusona.sdk.resources.dto.Trusonafication;
import com.trusona.sdk.resources.dto.TrusonaficationResult;
import com.trusona.sdk.resources.dto.TrusonaficationStatus;
import com.trusona.sdk.resources.exception.NoIdentityDocumentsException;
import com.trusona.sdk.resources.exception.TrusonaException;
import retrofit2.Response;

import java.time.Duration;
import java.util.UUID;

import static com.trusona.sdk.resources.dto.TrusonaficationStatus.IN_PROGRESS;

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
      public void handleErrors(Response response) throws TrusonaException {
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
    TrusonaficationService service = serviceGenerator.getService(TrusonaficationService.class);

    TrusonaficationResponse response = new CallHandler<TrusonaficationResponse>(service.getTrusonafication(trusonaficationId))
      .handle(defaultErrorHandler);

    while (response != null && IN_PROGRESS.equals(TrusonaficationStatus.valueOf(response.getStatus()))) {
      try {
        sleep(pollingInterval);
      }
      catch (InterruptedException e) {
        throw new TrusonaException("Thread was interrupted while polling for trusonafication result", e);
      }

      response = new CallHandler<TrusonaficationResponse>(service.getTrusonafication(trusonaficationId))
        .handle(defaultErrorHandler);
    }

    return response == null ? null : trusonaficationResultFromResponse(response);
  }

  private static TrusonaficationResult trusonaficationResultFromResponse(TrusonaficationResponse response) {
    String userIdentifier = response.getUserIdentifier();

    if (response.getTrusonaId() != null) {
      userIdentifier = LEGACY_PREFIX + response.getTrusonaId();
    }
    return new TrusonaficationResult(
      response.getId(),
      TrusonaficationStatus.valueOf(response.getStatus()),
      userIdentifier,
      response.getExpiresAt(),
      response.getResult() != null ? response.getResult().getBoundUserIdentifier() : null
    );
  }

  private static void sleep(Duration duration) throws InterruptedException {
    Thread.sleep(duration.toMillis());
  }
}
