package com.trusona.sdk.http

import retrofit2.Retrofit
import spock.lang.Specification

class EndpointMutatorSpec extends Specification {

  def "mutate should change the base url "() {
    given:
    def endpointUrl = "https://example.com/"
    def oldEndpoint = "https://example.net/"

    def retrofit = new Retrofit.Builder()
      .baseUrl(oldEndpoint)
      .build()

    def sut = new EndpointMutator(endpointUrl)

    when:
    def res = sut.mutate(retrofit.newBuilder())

    then:
    res.baseUrl().toString() == endpointUrl
  }
}
