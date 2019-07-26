package com.trusona.sdk.http.environment

import okhttp3.logging.HttpLoggingInterceptor
import spock.lang.Specification

class EuUatEnvironmentSpec extends Specification {
  EuUatEnvironment sut

  def setup() {
    sut = new EuUatEnvironment()
  }
  def "getLoggingLevel should return BASIC"() {
    when:
    def res = sut.getLoggingLevel()

    then:
    res == HttpLoggingInterceptor.Level.BASIC
  }


  def "getEndpointUrl should return the EU UAT url"() {
    when:
    def res = sut.getEndpointUrl()

    then:
    res == "https://api.staging.eu.trusona.net"
  }
}
