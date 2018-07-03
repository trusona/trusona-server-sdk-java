package com.trusona.sdk.config

import com.trusona.sdk.resources.dto.IdentityDocument
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Copyright Trusona, Inc.
 * Created on 1/23/18 for trusona-server-sdk.
 */
class JacksonConfigSpec extends Specification {

  @Unroll
  def "should parse iso8601 dates that look like #date"() {
    when:
    def res = JacksonConfig.dateFormat.parse(date)

    then:
    JacksonConfig.dateFormat.format(res) == '2018-01-23T23:28:45Z'

    where:
    date << ['2018-01-23T23:28:45.271Z', '2018-01-23T23:28:45.27Z', '2018-01-23T23:28:45.2Z', '2018-01-23T23:28:45Z']
  }

  def "should not include null values in JSON"() {
    when:
    def res = JacksonConfig.objectMapper.writeValueAsString(new IdentityDocument(id:UUID.randomUUID()))

    then:
    !res.contains("null")

  }
}
