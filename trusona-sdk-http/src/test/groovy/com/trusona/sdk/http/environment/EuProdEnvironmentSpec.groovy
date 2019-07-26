package com.trusona.sdk.http.environment

import okhttp3.logging.HttpLoggingInterceptor
import spock.lang.Specification

class EuProdEnvironmentSpec extends Specification {

  EuProdEnvironment sut

  def setup() {
    sut = new EuProdEnvironment()
  }

  def "getLoggingLevel should return NONE"() {
    when:
    def res = sut.getLoggingLevel()

    then:
    res.equals(HttpLoggingInterceptor.Level.NONE)
  }

  def "getEndpointUrl should return the EU prod url"() {
    when:
    def res = sut.getEndpointUrl()

    then:
    res == "https://api.eu.trusona.net"
  }
}
