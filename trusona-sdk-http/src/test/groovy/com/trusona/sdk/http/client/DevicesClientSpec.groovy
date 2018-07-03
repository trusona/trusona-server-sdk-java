package com.trusona.sdk.http.client

import com.trusona.sdk.resources.exception.TrusonaException
import spock.lang.Unroll

class DevicesClientSpec extends ClientSpec {

  DevicesClient sut

  def setup() {
    sut = new DevicesClient(mockServiceGenerator)
  }

  def "getDevice should return a device if found"() {
    given:
    def deviceIdentifier = 'johnny-5'
    def inactiveBody = """{
        "is_active": false
    }"""

    mockWebServer.enqueue(signedResponse(200, inactiveBody));

    when:
    def res = sut.getDevice(deviceIdentifier)
    def request = mockWebServer.takeRequest()

    then:
    request.method == 'GET'
    request.path == "/api/v2/devices/$deviceIdentifier"
    !res.active
  }

  def "getDevice should return null if not found"() {
    given:
    def deviceIdentifier = 'johnny-5'
    def notFoundBody = ''

    mockWebServer.enqueue(signedResponse(404, notFoundBody));

    when:
    def res = sut.getDevice(deviceIdentifier)
    def request = mockWebServer.takeRequest()

    then:
    request.method == 'GET'
    request.path == "/api/v2/devices/$deviceIdentifier"
    res == null
  }

  @Unroll
  def "getDevice should throw TrusonaException when the response status is #status"() {
    given:
    mockWebServer.enqueue(signedResponse(status, ''))

    when:
    sut.getDevice('foo-bar')

    then:
    thrown(TrusonaException)

    where:
    status << [400, 403, 500]
  }
}
