package com.trusona.sdk.http.client.security

import okhttp3.MediaType
import okhttp3.Request
import okhttp3.RequestBody
import spock.lang.Specification

class RequestHmacMessageSpec extends Specification {

  Request request = new Request.Builder()
    .url("https://jones.net/foo")
    .header("Content-Type", "application/json")
    .post(RequestBody.create(MediaType.parse("application/json"), '{"foo":"bar"}'))
    .header("X-Date", "foobar")
    .build()

  RequestHmacMessage sut = new RequestHmacMessage(request)

  def "getBodyDigest should return the md5 of the body of the request"() {
    when:
    def res = sut.getBodyDigest()

    then:
    res == "9bb58f26192e4ba00f01e2e7b136bbd8"
  }

  def "getContentType should return the content type of the request"() {
    when:
    def res = sut.getContentType()

    then:
    res == "application/json"
  }

  def "getContentType should return the content type of the request including charset"() {
    given:
    def withCharset = request.newBuilder()
      .header("Content-Type", "application/json;charset=utf-8")
      .post(RequestBody.create(MediaType.parse("application/json;charset=utf-8"), '{"foo":"bar"}'))
      .build()

    when:
    def res = new RequestHmacMessage(withCharset).getContentType()

    then:
    res == "application/json;charset=utf-8"
  }

  def "getDate should return the value of the X-Date header"() {
    when:
    def res = sut.getDate()

    then:
    res == "foobar"
  }

  def "getMethod should return the http method of the request"() {
    when:
    def res = sut.getMethod()

    then:
    res == "POST"
  }

  def "getRequestUri should return the uri of the request"() {
    when:
    def res = sut.getRequestUri()

    then:
    res == "/foo"
  }

  def "getRequestUri should include any query params"() {
    given:
    def withQuery = new Request.Builder()
      .url("http://jones.net/bar?fizz=buzz")
      .get()
      .build()

    when:
    def res = new RequestHmacMessage(withQuery).getRequestUri()

    then:
    res == "/bar?fizz=buzz"
  }
}
