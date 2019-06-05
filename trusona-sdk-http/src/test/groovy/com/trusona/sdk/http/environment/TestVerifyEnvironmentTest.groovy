package com.trusona.sdk.http.environment

import okhttp3.logging.HttpLoggingInterceptor
import spock.lang.Specification

class TestVerifyEnvironmentTest extends Specification {
  TestVerifyEnvironment sut

  def setup() {
    sut = new TestVerifyEnvironment()
  }

  def "getLoggingLevel should return BODY"() {
    when:
    def res = sut.getLoggingLevel()

    then:
    res.equals(HttpLoggingInterceptor.Level.BASIC)
  }

  def "getEndpointUrl should return the verify url"() {
    when:
    def res = sut.getEndpointUrl()

    then:
    res == "https://api.verify.trusona.net"
  }
}
