package com.trusona.sdk.http

import com.trusona.sdk.resources.exception.TrusonaException
import retrofit2.Response
import retrofit2.mock.Calls
import spock.lang.Specification

import static com.trusona.sdk.http.Fixtures.jsonBody

class CallHandlerSpec extends Specification {

  def "handle should return the body on success"() {
    given:
    def call = Calls.response("success")

    when:
    def res = new CallHandler<String>(call).handle()

    then:
    res == "success"
  }

  def "handle should call error handlers when call is not successful"() {
    given:
    def response = Response.error(500, jsonBody("{}"))
    def call = Calls.response(response)
    ErrorHandler firstErrorHandler = Mock()
    ErrorHandler secondErrorHandler = Mock()

    when:
    new CallHandler(call).handle(firstErrorHandler, secondErrorHandler)

    then:
    1 * firstErrorHandler.handleErrors(response)
    1 * secondErrorHandler.handleErrors(response)
  }

  def "handle should wrap an IOException in a TrusonaException"() {
    given:
    def ioException = new IOException()
    def call = Calls.failure(ioException)

    when:
    new CallHandler(call).handle()

    then:
    def exc = thrown(TrusonaException)
    exc.message == "A network related error occurred. You should double check that you can connect to Trusona and try your request again."
    exc.cause == ioException
  }
}
