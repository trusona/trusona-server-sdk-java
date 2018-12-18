package com.trusona.sdk.http.environment

import okhttp3.logging.HttpLoggingInterceptor
import spock.lang.Specification

class ProdEnvironmentSpec extends Specification {

  ProdEnvironment sut

  def setup() {
    sut = new ProdEnvironment()
  }

  def "getLoggingLevel should return NONE"() {
    when:
    def res = sut.getLoggingLevel()

    then:
    res.equals(HttpLoggingInterceptor.Level.NONE)
  }

  def "getEndpointUrl should return the prod url"() {
    when:
    def res = sut.getEndpointUrl()

    then:
    res == "https://api.trusona.net"
  }
}
