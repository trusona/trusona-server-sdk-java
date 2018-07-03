package com.trusona.sdk.http.client.v2.service;

import com.trusona.sdk.resources.dto.IdentityDocument;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;
import java.util.UUID;

public interface IdentityDocumentService {
  @GET("/api/v2/identity_documents/{id}")
  Call<IdentityDocument> getIdentityDocument(@Path("id") UUID id);

  @GET("/api/v2/identity_documents")
  Call<List<IdentityDocument>> findIdentityDocuments(@Query("user_identifier") String userIdentifier);
}
