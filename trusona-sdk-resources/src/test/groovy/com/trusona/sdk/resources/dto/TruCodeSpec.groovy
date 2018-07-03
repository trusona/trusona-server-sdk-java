package com.trusona.sdk.resources.dto

class TruCodeSpec extends DtoSpec<TruCode> {

  TruCode sut = new TruCode(
    id: UUID.randomUUID(),
    identifier: "foobar"
  )

  String json = """
  {
    "id": "${sut.id.toString()}",
    "identifier": "foobar"
  }
  """
}
