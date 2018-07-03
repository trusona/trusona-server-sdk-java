package com.trusona.sdk.http.client.security

import spock.lang.Specification

class DefaultHmacSignatureGeneratorSpec extends Specification {

  DefaultHmacSignatureGenerator sut

  def setup() {
    sut = new DefaultHmacSignatureGenerator()
  }

  def "getSignature should return signature"() {
    given:
    def expectedSignature = "YTgwNDgzNGRjNTA0YjBkYWJmNmFlMzU0MjJiNmRmYTRjNjk5NTQxMDk3MGFkN2YzZjlmZTYyMjdlMTlkNjc4Zg=="
    def secret            = "7f1dd753b6fa473d07c99b56d43bd5da3cd928487d5022e1810fab96c70945b01ad2603585542d33a1383b1f14b5880373474ff40c76a38df19052cefeb3a3eb"

    def mockRequest = Mock(HmacMessage)

    mockRequest.getBodyDigest()   >> "d41d8cd98f00b204e9800998ecf8427e"
    mockRequest.getRequestUri()   >> "/test/auth?blah=blah"
    mockRequest.getContentType()  >> "application/json"
    mockRequest.getDate()         >> "Tue, 27 Jun 2017 18:03:47 GMT"
    mockRequest.getMethod()       >> "GET"

    when:
    def res = sut.getSignature(mockRequest, secret)

    then:
    res == expectedSignature
  }
}
