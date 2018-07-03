package com.trusona.sdk.http.client.v2.response

import com.trusona.sdk.http.client.RequestResponseSpec

class DiscoverableConfigResponseSpec extends RequestResponseSpec<DiscoverableConfigResponse> {

  String json = """{
    "endpoints": [ "foo", "bar" ]
   }"""

  DiscoverableConfigResponse sut = new DiscoverableConfigResponse(endpoints: ["foo", "bar"])
}
