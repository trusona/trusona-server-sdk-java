package com.trusona.sdk

import com.trusona.sdk.config.JacksonConfig
import spock.lang.IgnoreIf
import spock.lang.Specification

@IgnoreIf({ !System.getenv('TRUSONA_TOKEN') && !System.getenv('TRUSONA_SECRET') })
class TrusonaIntegrationSpec extends Specification {
  static String TRUSONA_TOKEN = 'TRUSONA_TOKEN'
  static String TRUSONA_SECRET = 'TRUSONA_SECRET'

  def "should be able to get a device"() {
    given:
    def deviceIdentifier = "kC_9iF_CNcJqdU4PvJspx6okdQnxJsYNteL0EJG_O-c"

    expect:
    def res = trusona.getDevice(deviceIdentifier)

    res.active
    res.activatedAt == JacksonConfig.dateFormat.parse("2018-01-12T21:36:17.833Z")

    where:
    trusona << [
        new Trusona(
            System.getenv(TRUSONA_TOKEN),
            System.getenv(TRUSONA_SECRET),
            TrusonaEnvironment.UAT
        ),
        new Trusona(
            System.getenv(TRUSONA_TOKEN),
            System.getenv(TRUSONA_SECRET),
            'https://api.staging.trusona.net'
        )
    ]
  }
}
