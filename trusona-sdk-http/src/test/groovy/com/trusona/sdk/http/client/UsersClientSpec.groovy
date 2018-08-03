package com.trusona.sdk.http.client

import com.trusona.sdk.resources.exception.TrusonaException
import com.trusona.sdk.resources.exception.UserNotFoundException
import spock.lang.Unroll

class UsersClientSpec extends ClientSpec {

  UsersClient sut

  def setup() {
    sut = new UsersClient(mockServiceGenerator)
  }

  def "deactivateUser should not throw an error when it was successful"() {
    given:
    def userIdentifier = 'johnny-5'
    def successBody = ''

    mockWebServer.enqueue(signedResponse(204, successBody));

    when:
    def res = sut.deactivateUser(userIdentifier)
    def request = mockWebServer.takeRequest()

    then:
    request.method == 'DELETE'
    request.path == "/api/v2/users/$userIdentifier"
    res == null
  }

  def "deactivateUser should throw a UserNotFoundException when the response status is 404"() {
    given:
    def userIdentifier = 'johnny-5'
    def notFoundBody = ''

    mockWebServer.enqueue(signedResponse(404, notFoundBody));

    when:
    sut.deactivateUser(userIdentifier)

    then:
    thrown(UserNotFoundException)
  }

  @Unroll
  def "deactivateUser should throw a TrusonaException when the response status is #status"() {
    given:
    mockWebServer.enqueue(signedResponse(status, ''))

    when:
    sut.deactivateUser('foo-bar')

    then:
    thrown(TrusonaException)

    where:
    status << [400, 403, 500]
  }
}
