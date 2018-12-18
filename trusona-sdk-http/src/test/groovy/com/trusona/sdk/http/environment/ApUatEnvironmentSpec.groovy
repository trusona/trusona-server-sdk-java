package com.trusona.sdk.http.environment

import okhttp3.logging.HttpLoggingInterceptor
import spock.lang.Specification

class ApUatEnvironmentSpec extends Specification {

  ApUatEnvironment sut

  def setup() {
    sut = new ApUatEnvironment()
  }
  def "getLoggingLevel should return BASIC"() {
    when:
    def res = sut.getLoggingLevel()

    then:
    res == HttpLoggingInterceptor.Level.BASIC
  }


  def "getEndpointUrl should return the AP UAT url"() {
    when:
    def res = sut.getEndpointUrl()

    then:
    res == "https://api.staging.ap.trusona.net"
  }
}

