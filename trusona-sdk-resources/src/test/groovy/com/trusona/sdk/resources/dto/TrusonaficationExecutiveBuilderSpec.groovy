package com.trusona.sdk.resources.dto

import spock.lang.Specification

/**
 * Copyright Trusona, Inc.
 * Created on 1/22/18 for trusona-server-sdk.
 */
class TrusonaficationExecutiveBuilderSpec extends Specification {

  def sut = new Trusonafication.ExecutiveBuilder()

  def "constructor should have desiredLevel set to 3"() {
    expect:
    sut.trusonafication.desiredLevel == 3
  }

  def "constructor should have showIdentityDocument set to true"() {
    expect:
    sut.trusonafication.showIdentityDocument
  }

  def "deviceIdentifier should set the deviceIdentifier and return the builder"() {
    when:
    def res = sut.deviceIdentifier('wall-e')

    then:
    sut.trusonafication.deviceIdentifier == 'wall-e'
    res == sut
  }

  def "emailAddress should set the emailAddress and return the builder"() {
    when:
    def res = sut.email('taco@jones.com')

    then:
    sut.trusonafication.email == 'taco@jones.com'
    res == sut
  }

  def "action should set the action and return the builder"() {
    when:
    def res = sut.action('jackson')

    then:
    sut.trusonafication.action == 'jackson'
    res == sut
  }

  def "resource should set the resource and return the builder"() {
    when:
    def res = sut.resource('human')

    then:
    sut.trusonafication.resource == 'human'
    res == sut
  }

  def "callbackUrl should set the callbackUrl and return the builder"() {
    when:
    def res = sut.callbackUrl('https://earl.grey.com')

    then:
    sut.trusonafication.callbackUrl == 'https://earl.grey.com'
    res == sut
  }

  def "expiresAt should set the expiresAt and return the builder"() {
    given:
    def date = new Date()

    when:
    def res = sut.expiresAt(date)

    then:
    sut.trusonafication.expiresAt == date
    res == sut
  }

  def "withoutPrompt should set the prompt to false and return the builder"() {
    when:
    def res = sut.withoutPrompt()

    then:
    !sut.trusonafication.prompt
    res == sut
  }

  def "build should return the underlying trusonafication"() {
    when:
    def res = sut.build()

    then:
    res == sut.trusonafication
  }
}
