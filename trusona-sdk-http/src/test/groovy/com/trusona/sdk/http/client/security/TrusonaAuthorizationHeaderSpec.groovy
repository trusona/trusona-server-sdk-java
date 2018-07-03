package com.trusona.sdk.http.client.security

import spock.lang.Specification

class TrusonaAuthorizationHeaderSpec extends Specification {
  TrusonaAuthorizationHeader sut

  def setup() {
    sut = new TrusonaAuthorizationHeader("test.test.test", "tacos")
  }

  def "parseAuthorizationHeader should return a valid and parsed header object when headerValue is well formatted"() {
    given:
    def headerValue = "jones test.test.test:tacos"

    when:
    def res = TrusonaAuthorizationHeader.parseAuthorizationHeader(headerValue)

    then:
    res.isValid()
    res.token == "test.test.test"
    res.signature == "tacos"
  }

  def "parseAuthorizationHeader should return an invalid header object when headerValue is not well formatted"() {
    given:
    def headerValue = "notJones"

    when:
    def res = TrusonaAuthorizationHeader.parseAuthorizationHeader(headerValue)

    then:
    !res.isValid()
  }


  def "toString should return a valid header value"() {
    when:
    def res = sut.toString()

    then:
    res == "TRUSONA test.test.test:tacos"
  }
}
