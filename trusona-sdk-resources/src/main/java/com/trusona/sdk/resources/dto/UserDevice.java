package com.trusona.sdk.resources.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.trusona.sdk.resources.TrusonaApi;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * A representation of a binding between a Trusona enabled device and a user.
 */
public class UserDevice extends BaseDto {
  private static final long serialVersionUID = -1205435826939238635L;

  @JsonProperty("id")
  private String activationCode;
  private String userIdentifier;
  private String deviceIdentifier;
  private boolean active;

  /**
   * Gets the activation code for this binding. Use this activation code to activate the binding, allowing the user to
   * perform Trusonafications.
   *
   * @return the activation code.
   */
  public String getActivationCode() {
    return activationCode;
  }

  /**
   * Gets the identifier for the user. This will be the same value provided to {@link TrusonaApi#createUserDevice(String, String) createUserDevice}.
   *
   * @return the user identifier.
   */
  public String getUserIdentifier() {
    return userIdentifier;
  }

  /**
   * Gets the unique identifier for the device. This will be the same value provided to {@link TrusonaApi#createUserDevice(String, String)} createUserDevice}.
   *
   * @return the device identifier.
   */
  public String getDeviceIdentifier() {
    return deviceIdentifier;
  }

  /**
   * Whether or not the binding is active. An active binding can be used to complete Trusonafications.
   *
   * @return true if the user is active.
   */
  public boolean isActive() {
    return active;
  }

  public void setActivationCode(String activationCode) {
    this.activationCode = activationCode;
  }

  public void setUserIdentifier(String userIdentifier) {
    this.userIdentifier = userIdentifier;
  }

  public void setDeviceIdentifier(String deviceIdentifier) {
    this.deviceIdentifier = deviceIdentifier;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(29385, 2771621, this);
  }
}
