package com.trusona.sdk.http.client

import com.trusona.sdk.resources.exception.TrusonaException
import retrofit2.Response
import spock.lang.Specification

import static com.trusona.sdk.http.Fixtures.jsonBody

class BaseErrorHandlerSpec extends Specification {

  def sut = new BaseErrorHandler() {
    @Override
    void handleErrors(Response response) throws TrusonaException {
    }
  }

  def "getErrorResponse should parse the error body"() {
    given:
    def errorBody = '''\
    {
      "error": "this is an error",
      "message": "A message for your error",
      "description": "how about a long description too"
   
    }
    '''

    def response = Response.error(422, jsonBody(errorBody))

    when:
    def res = sut.getErrorResponse(response)
    then:
    res.error == "this is an error"
    res.message == "A message for your error"
    res.description == "how about a long description too"
  }

  def "getErrorResponse should parse the error body with field errors"() {
    given:
    def errorBody = '''\
    {
      "error": "this is an error",
      "message": "A message for your error",
      "description": "how about a long description too",
      "field_errors": {
        "foo": ["fizz", "buzz"]
       }
    }
    '''

    def response = Response.error(422, jsonBody(errorBody))

    when:
    def res = sut.getErrorResponse(response)

    then:
    res.error == "this is an error"
    res.message == "A message for your error"
    res.description == "how about a long description too"
    res.fieldErrors.get("foo").containsAll(["fizz", "buzz"])
  }

  def "getErrorResponse should throw a TrusonaException when it cannot parse error body"() {
    given:
    def response = Response.error(500, jsonBody(""))

    when:
    def res = sut.getErrorResponse(response)

    then:
    thrown(TrusonaException)
  }
}
