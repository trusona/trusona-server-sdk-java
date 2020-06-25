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

  def 'getCustomEnvironment should return environment with endpoint url'() {
    given:
    def sut = EnvironmentFactory.getCustomEnvironment('https://localhost:8080')

    expect:
    sut == new CustomEnvironment('https://localhost:8080')
  }

  def "getCustomEnvironment should return IllegalArgumentException for null endpoint url"() {
    when:
    EnvironmentFactory.getCustomEnvironment(null)

    then:
    thrown(IllegalArgumentException)
  }
}
