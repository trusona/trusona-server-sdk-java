package com.trusona.sdk.resources.dto

import com.trusona.sdk.config.JacksonConfig

class DeviceSpec extends DtoSpec<Device> {

  String json = """{
      "activated_at": "2018-01-23T23:28:45Z",
      "is_active": true
  }"""

  Device sut = new Device(
    activatedAt: JacksonConfig.dateFormat.parse("2018-01-23T23:28:45Z"),
    active: true
  )
}
