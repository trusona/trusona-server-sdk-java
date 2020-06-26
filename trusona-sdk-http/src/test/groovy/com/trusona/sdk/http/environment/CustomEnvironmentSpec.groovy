package com.trusona.sdk.http.environment

import okhttp3.logging.HttpLoggingInterceptor
import spock.lang.Specification

class CustomEnvironmentSpec extends Specification {

  CustomEnvironment sut

  def setup() {
    sut = new CustomEnvironment('http://localhost:8080')
  }

  def "getLoggingLevel should return NONE"() {
    when:
    def res = sut.getLoggingLevel()

    then:
    res.equals(HttpLoggingInterceptor.Level.BASIC)
  }

  def "getEndpointUrl should return the custom url that was set"() {
    when:
    def res = sut.getEndpointUrl()

    then:
    res == "http://localhost:8080"
  }
}
