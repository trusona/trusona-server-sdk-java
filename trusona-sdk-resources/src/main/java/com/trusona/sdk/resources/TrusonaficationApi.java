package com.trusona.sdk.resources;

import com.trusona.sdk.resources.dto.Trusonafication;
import com.trusona.sdk.resources.dto.TrusonaficationResult;
import com.trusona.sdk.resources.exception.TrusonaException;

import java.time.Duration;
import java.util.UUID;

public interface TrusonaficationApi {
  TrusonaficationResult createTrusonafication(Trusonafication trusonafication) throws TrusonaException;

  TrusonaficationResult getTrusonaficationResult(UUID trusonaficationId) throws TrusonaException;

  TrusonaficationResult pollTrusonaficationResult(UUID trusonaficationId, Duration timeout) throws TrusonaException;

  Void cancelTrusonafication(UUID trusonaficationId) throws TrusonaException;
}
