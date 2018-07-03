package com.trusona.sdk.resources.dto

class UserDeviceSpec extends DtoSpec<UserDevice> {

  UserDevice sut = new UserDevice(
    userIdentifier: "user",
    deviceIdentifier: "device",
    activationCode: UUID.randomUUID(),
    active: true
  )

  String json = """
  {
    "user_identifier": "user",
    "device_identifier": "device",
    "id": "${sut.activationCode.toString()}",
    "active": true
  }
  """
}
