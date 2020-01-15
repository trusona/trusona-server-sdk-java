package com.trusona.sdk.resources.dto

import spock.lang.Specification
import spock.lang.Unroll

import static com.trusona.sdk.resources.dto.AuthenticatorType.MOBILE_APP
import static com.trusona.sdk.resources.dto.TrusonaficationStatus.*
import static java.util.UUID.randomUUID

class TrusonaficationResultSpec extends Specification {

  @Unroll
  def "isSuccessful should return #expected when the status is #status.inspect()"() {
    given:
    def sut = new TrusonaficationResult(
        randomUUID(),
        status,
        'blars-tacoman',
        new Date(),
        null,
        new Date(),
        new Date(),
        MOBILE_APP
    )

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
