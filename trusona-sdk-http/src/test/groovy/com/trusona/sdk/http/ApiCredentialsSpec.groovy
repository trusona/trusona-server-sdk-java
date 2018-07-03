package com.trusona.sdk.http

import spock.lang.Specification

class ApiCredentialsSpec extends Specification {

  def "getParsedToken should parse a token"() {
    given:
    def token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJ0cnVhZG1pbi5hcGkudHJ1c29uYS5jb20iLCJzdWIiOiIwZjAzNDhmMC00NmQ2LTQ3YzktYmE0ZC0yZTdjZDdmODJlM2UiLCJhdWQiOiJhcGkudHJ1c29uYS5jb20iLCJleHAiOjE1MTk4ODU0OTgsImlhdCI6MTQ4ODMyNzg5OCwianRpIjoiNzg4YWYwNzAtNDBiOS00N2MxLWE3ZmUtOGUwZmE1NWUwMDE1IiwiYXRoIjoiUk9MRV9UUlVTVEVEX1JQX0NMSUVOVCJ9.2FNvjG9yB5DFEcNijk8TryRtKVffiDARRcRIb75Z_Pp85MxW63rhzdLFIN6PtQ1Tzb8lHPPM_4YOe-feeLOzWw"
    def sut = new ApiCredentials(token, "secret")

    when:
    def res = sut.parsedToken

    then:
    res.id == UUID.fromString("788af070-40b9-47c1-a7fe-8e0fa55e0015")
    res.authorities == "ROLE_TRUSTED_RP_CLIENT"
    res.audience == "api.trusona.com"
    res.expiredAt == 1519885498
    res.issuedAt == 1488327898
    res.issuer == "truadmin.api.trusona.com"
    res.subject == UUID.fromString("0f0348f0-46d6-47c9-ba4d-2e7cd7f82e3e")
  }
}
