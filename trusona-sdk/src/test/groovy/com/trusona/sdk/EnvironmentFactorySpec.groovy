package com.trusona.sdk

import com.trusona.sdk.http.environment.*
import spock.lang.Specification

import static com.trusona.sdk.TrusonaEnvironment.*

class EnvironmentFactorySpec extends Specification {

  def "getEnvironment should return the correct environment for #env"() {
    expect:
    EnvironmentFactory.getEnvironment(env).class == envClass.class

    where:
    env           | envClass
    PRODUCTION    | new ProdEnvironment()
    UAT           | new UatEnvironment()
    AP_PRODUCTION | new ApProdEnvironment()
    AP_UAT        | new ApUatEnvironment()
    EU_PRODUCTION | new EuProdEnvironment()
    EU_UAT        | new EuUatEnvironment()
    TEST_VERIFY   | new TestVerifyEnvironment()
  }

  def "getEnvironment should return IllegalArgumentException for unknown environments"() {
    when:
    EnvironmentFactory.getEnvironment(null)

    then:
    thrown(IllegalArgumentException)
  }
}
