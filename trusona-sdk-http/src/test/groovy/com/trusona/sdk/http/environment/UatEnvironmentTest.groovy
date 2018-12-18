package com.trusona.sdk.http.environment

import okhttp3.logging.HttpLoggingInterceptor
import spock.lang.Specification

class UatEnvironmentTest extends Specification {
  UatEnvironment sut

  def setup() {
    sut = new UatEnvironment()
  }

  def "getLoggingLevel should return BODY"() {
    when:
    def res = sut.getLoggingLevel()

    then:
    res.equals(HttpLoggingInterceptor.Level.BASIC)
  }

  def "getEndpointUrl should return the prod url"() {
    when:
    def res = sut.getEndpointUrl()

    then:
    res == "https://api.staging.trusona.net"
  }
}
