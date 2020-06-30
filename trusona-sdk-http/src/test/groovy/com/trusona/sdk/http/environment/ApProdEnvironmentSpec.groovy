package com.trusona.sdk.http.environment

import okhttp3.logging.HttpLoggingInterceptor
import spock.lang.Specification

class ApProdEnvironmentSpec extends Specification {

  ApProdEnvironment sut

  def setup() {
    sut = new ApProdEnvironment()
  }

  def "getLoggingLevel should return NONE"() {
    when:
    def res = sut.getLoggingLevel()

    then:
    res == HttpLoggingInterceptor.Level.NONE
  }

  def "getEndpointUrl should return the AP prod url"() {
    when:
    def res = sut.getEndpointUrl()

    then:
    res == "https://api.ap.trusona.net"
  }
}
