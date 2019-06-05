package com.trusona.sdk.http.environment

import okhttp3.logging.HttpLoggingInterceptor
import spock.lang.Specification

class VerifyEnvironmentTest extends Specification {
  VerifyEnvironment sut

  def setup() {
    sut = new VerifyEnvironment()
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
