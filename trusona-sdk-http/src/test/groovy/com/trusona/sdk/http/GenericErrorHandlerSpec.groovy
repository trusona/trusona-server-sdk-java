package com.trusona.sdk.http

import com.trusona.sdk.resources.exception.TrusonaException
import com.trusona.sdk.resources.exception.ValidationException
import retrofit2.Response
import spock.lang.Specification
import spock.lang.Unroll

import static com.trusona.sdk.http.Fixtures.jsonBody

class GenericErrorHandlerSpec extends Specification {

  GenericErrorHandler sut = new GenericErrorHandler()

  def "handleErrors should return a ValidationException when fieldErrors are present"() {
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
    sut.handleErrors(response)

    then:
    def exc = thrown(ValidationException)
    exc.message == "how about a long description too"
    exc.fieldErrors.get("foo").containsAll(["fizz", "buzz"])
  }

  def "handleErrors should return a TrusonaClientException when a status code doesn't match the predefined codes"() {
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

    def response = Response.error(415, jsonBody(errorBody))

    when:
    sut.handleErrors(response)

    then:
    def exc = thrown(TrusonaException)
    exc.message == "how about a long description too"
  }

  def "handleErrors should return a TrusonaClientException with generic verbiage when error body cannot be parsed as JSON"() {
    given:
    def response = Response.error(415, jsonBody(""))

    when:
    sut.handleErrors(response)

    then:
    def exc = thrown(TrusonaException)
    exc.message == "An unknown error occurred. Contact Trusona to determine the exact cause."
  }

  @Unroll
  def "handleErrors should return a TrusonaClientException with appropriate verbiage when it gets a #statusCode"() {
    given:
    def response = Response.error(statusCode, jsonBody(""))

    when:
    sut.handleErrors(response)

    then:
    def exc = thrown(TrusonaException)
    exc.message == message

    where:
    statusCode | message
    400        | "The Trusona SDK was unable to fulfill your request do to an error with the SDK. Contact Trusona to determine the issue."
    403        | "The token and/or secret you are using are invalid. Contact Trusona to get valid Server SDK credentials."
    500        | "The server was unable to process your request at this time. Feel free to try your request again later."
    502        | "The server was unable to process your request at this time. Feel free to try your request again later."
    503        | "The server was unable to process your request at this time. Feel free to try your request again later."
    504        | "The server was unable to process your request at this time. Feel free to try your request again later."
  }
}
