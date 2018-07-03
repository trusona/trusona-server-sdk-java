package com.trusona.sdk.http.client.security

import okhttp3.*
import spock.lang.Specification

class ResponseHmacMessageSpec extends Specification {

  String mediaType = "application/json"
  String date = "01/02/2018T12:23:33Z"
  String body = '{"foo":"bar"}'

  Request request = new Request.Builder()
    .url("https://jones.net/foo")
    .header("Content-Type", "application/json")
    .post(RequestBody.create(MediaType.parse("application/json"), body))
    .header("X-Date", "foobar")
    .build()

  Response response = new Response.Builder()
    .request(request)
    .code(200)
    .message("OK")
    .protocol(Protocol.HTTP_1_1)
    .body(ResponseBody.create(MediaType.parse(mediaType), body))
    .header("Content-Type", mediaType)
    .header("X-Date", date)
    .build()

  ResponseHmacMessage sut = new ResponseHmacMessage(response)

  def "getBodyDigest should return the md5 of the body of the request"() {
    when:
    def res = sut.getBodyDigest()

    then:
    res == "9bb58f26192e4ba00f01e2e7b136bbd8"
  }

  def "getBodyDigest should return the md5 of '' when there is no body"() {
    given:
    def noBodyResponse = response.newBuilder().body(null).build()

    when:
    def res = new ResponseHmacMessage(noBodyResponse).getBodyDigest()

    then:
    res == "d41d8cd98f00b204e9800998ecf8427e"

  }

  def "getContentType should return the content type of the request"() {
    when:
    def res = sut.getContentType()

    then:
    res == mediaType
  }

  def "getContentType should return the content type of the request including charset"() {
    given:
    def withCharset = response.newBuilder()
      .header("Content-Type", "application/json;charset=utf-8")
      .body(ResponseBody.create(MediaType.parse("application/json;charset=utf-8"), body))
      .build()

    when:
    def res = new ResponseHmacMessage(withCharset).getContentType()

    then:
    res == "application/json;charset=utf-8"
  }

  def "getContentType should return an empty string when there is no Content-Type header set"() {
    given:
    def noContentType = response.newBuilder().removeHeader("Content-Type").build()

    when:
    def res = new ResponseHmacMessage(noContentType).getContentType()

    then:
    res == ""
  }

  def "getDate should return the value of the X-Date header"() {
    when:
    def res = sut.getDate()

    then:
    res == date
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
    def res = new ResponseHmacMessage(response.newBuilder().request(withQuery).build()).getRequestUri()

    then:
    res == "/bar?fizz=buzz"
  }
}
