package com.trusona.sdk.resources.dto

import spock.lang.Specification
import spock.lang.Unroll

import static com.trusona.sdk.resources.dto.TrusonaficationStatus.*

/**
 * Copyright Trusona, Inc.
 * Created on 1/29/18 for trusona-server-sdk.
 */
class TrusonaficationResultSpec extends Specification {

  @Unroll
  def "isSuccessful should return #expected when the status is #status.inspect()"() {
    given:
    def sut = new TrusonaficationResult(UUID.randomUUID(), status, 'blars-tacoman', new Date())

    when:
    def res = sut.isSuccessful()

    then:
    res == expected

    where:
    expected | status
    true     | ACCEPTED
    true     | ACCEPTED_AT_HIGHER_LEVEL
    false    | REJECTED
    false    | EXPIRED
    false    | IN_PROGRESS
    false    | INVALID
    false    | ACCEPTED_AT_LOWER_LEVEL
    false    | null
  }
}
