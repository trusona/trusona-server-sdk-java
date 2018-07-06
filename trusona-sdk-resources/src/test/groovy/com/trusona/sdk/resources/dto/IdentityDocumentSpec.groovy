package com.trusona.sdk.resources.dto

import static com.trusona.sdk.config.JacksonConfig.getDateFormat

class IdentityDocumentSpec extends DtoSpec<IdentityDocument> {

  IdentityDocument sut = new IdentityDocument(
    id: UUID.fromString('96ea5830-8e5e-42c5-9cbb-8a941d2ff7f8'),
    hash: "foobar",
    verifiedAt: dateFormat.parse('2018-01-23T23:28:45Z'),
    verificationStatus: VerificationStatus.UNVERIFIED,
    type: "AAMVA_DRIVERS_LICENSE"
  )

  String json = """
  {
    "id": "96ea5830-8e5e-42c5-9cbb-8a941d2ff7f8",
    "hash": "foobar",
    "verification_status": "UNVERIFIED",
    "verified_at": "2018-01-23T23:28:45Z",
    "type": "AAMVA_DRIVERS_LICENSE"
  }
  """
}
