package com.trusona.sdk.http.client.security

import com.trusona.sdk.config.JacksonConfig
import spock.lang.Specification

class ParsedTokenSpec extends Specification {

  String json = """{
    "ath": "ROLE_TRUSTED_RP_CLIENT",
    "aud": "api.trusona.com",
    "exp": 1519885498,
    "iat": 1488327898,
    "iss": "truadmin.api.trusona.com",
    "jti": "788af070-40b9-47c1-a7fe-8e0fa55e0015",
    "sub": "0f0348f0-46d6-47c9-ba4d-2e7cd7f82e3e"
    }"""

  def "can be parsed from a token"() {
    when:
    def res = JacksonConfig.objectMapper.readValue(json, ParsedToken)

    then:
    res.id == UUID.fromString("788af070-40b9-47c1-a7fe-8e0fa55e0015")
    res.authorities == "ROLE_TRUSTED_RP_CLIENT"
    res.audience == "api.trusona.com"
    res.expiredAt == 1519885498
    res.issuedAt == 1488327898
    res.issuer == "truadmin.api.trusona.com"
    res.subject == UUID.fromString("0f0348f0-46d6-47c9-ba4d-2e7cd7f82e3e")
  }

  def "can handle really big expired times"() {
    given:
    def json = '''{"sub":"31f44568-fbd3-428c-80f6-d0cf3d9edc82","nbf":1521822832,"ath":"SOME_ROLE","iss":"ts:9f8f5920-ac4a-415f-a810-b37f96993bdf","exp":4643886832,"iat":1521822832,"jti":"5e4931bc-823b-4d7a-b388-7badb9f09cbb"}'''

    when:
    def res = JacksonConfig.objectMapper.readValue(json, ParsedToken)

    then:
    res.expiredAt == 4643886832

  }

}
