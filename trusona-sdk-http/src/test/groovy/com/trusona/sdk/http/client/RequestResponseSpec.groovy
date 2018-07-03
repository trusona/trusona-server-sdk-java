package com.trusona.sdk.http.client

import com.fasterxml.jackson.databind.ObjectMapper
import com.trusona.sdk.config.JacksonConfig
import spock.lang.Specification

abstract class RequestResponseSpec<T> extends Specification {
  public static final List<String> BLANK_STRINGS = [null, '', '      ']

  ObjectMapper mapper = JacksonConfig.objectMapper

  boolean isSkipSerialization() { return false }

  Class<? super T> getReadClass() {
    getSut().class
  }

  abstract T getSut()

  abstract String getJson()

  String toJson(T object) {
    mapper.writeValueAsString(object)
  }

  T fromJson(String json) {
    (T) mapper.readValue(json, getReadClass())
  }

  String normalizeJson(String json) {
    toJson(fromJson(json))
  }

  boolean isSerializable(T object, String expectedJson) {
    normalizeJson(expectedJson) == normalizeJson(toJson(object))
  }

  boolean isDeserializable(String json, T expectedObject) {
    expectedObject == fromJson(json)
  }

  def "should be serializable to JSON"() {
    expect:
    skipSerialization || normalizeJson(toJson(getSut())) == normalizeJson(getJson())
  }

  def "should be deserializable from JSON"() {
    expect:
    getSut() == fromJson(getJson())
  }

  def "should ignore unknown properties on deserialization"() {
    expect:
    getSut() == fromJson(getJson().replaceFirst('\\{', '{"jones": "good boy",'))
  }
}
