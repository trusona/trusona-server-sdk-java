package com.trusona.sdk.resources.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Date;
import java.util.UUID;

/**
 * A representation of an authentication request to be sent to a Trusona enabled device/user. Use the
 * {@link Trusonafication#essential()} method to build an Essential level trusonafication.
 */
public class Trusonafication extends BaseDto {
  private static final long serialVersionUID = 4438341104830942514L;

  private int desiredLevel;
  private String deviceIdentifier;
  @JsonProperty("trucode_id")
  private UUID truCodeId;
  private String userIdentifier;
  private String action;
  private String resource;
  private Date expiresAt;
  private boolean userPresence;
  private boolean prompt;
  private boolean showIdentityDocument;
  private Map<String, Object> customFields;

  @JsonProperty("email")
  private String emailAddress;

  private Trusonafication() {
    this.userPresence = true;
    this.prompt = true;
  }

  public int getDesiredLevel() {
    return desiredLevel;
  }

  public String getDeviceIdentifier() {
    return deviceIdentifier;
  }

  public UUID getTruCodeId() {
    return truCodeId;
  }

  public String getUserIdentifier() {
    return userIdentifier;
  }

  public String getAction() {
    return action;
  }

  public String getResource() {
    return resource;
  }

  public Date getExpiresAt() {
    return expiresAt;
  }

  public boolean isUserPresence() {
    return userPresence;
  }

  public boolean isPrompt() {
    return prompt;
  }

  public boolean isShowIdentityDocument() {
    return showIdentityDocument;
  }

  public Map<String, Object> getCustomFields() {
    return customFields;
  }

  public String getEmail() {
    return emailAddress;
  }

  public interface IdentifierStep {
    /**
     * Sets the device identifier of the user to be authenticated.
     *
     * @param deviceIdentifier the user's device identifier.
     * @return the next step required to finish building the trusonafication.
     */
    ActionStep deviceIdentifier(String deviceIdentifier);

    /**
     * Sets the TruCode ID that was scanned by a Trusona enabled device. The TruCode ID will be used to look up the
     * device identifier that performed the scan.
     *
     * @param truCodeId a TruCode ID scanned by a Trusona enabled device.
     * @return the next step required to finish building the trusonafication.
     */
    ActionStep truCode(UUID truCodeId);

    /**
     * Sets the user identifier of the user to be authenticated.
     *
     * @param userIdentifier the user's identifier.
     * @return the next step required to finish building the trusonafication.
     */
    ActionStep userIdentifier(String userIdentifier);

    /**
     * Sets the user identifier of the user to be authenticated.
     *
     * @param emailAddress the user's email address
     * @return the next step required to finish building the trusonafication.
     */
    ActionStep email(String emailAddress);
  }

  public interface ActionStep {
    /**
     * Sets the action that the user is attempting to perform.
     *
     * @param action the action.
     * @return the next step required to finish building the trusonafication.
     */
    ResourceStep action(String action);
  }

  public interface ResourceStep {
    /**
     * Sets the resource that the user is taking action on.
     *
     * @param resource the resource.
     * @return the next step required to finish building the trusonafication.
     */
    FinalizeStep resource(String resource);
  }


  public interface FinalizeStep {
    /**
     * Sets the flag on whether to require the user's approval to false.
     *
     * @return the next step required to finish building the trusonafication.
     */
    FinalizeStep withoutPrompt();

    /**
     * Sets the flag on whether or not the user is required to prove their presence to false. The user proves their
     * presence by proving they can perform the action required to unlock their phone, using whatever mechanism (PIN,
     * pattern, biometric, etc) the user has configured for their device. If no mechanism has been configured the user
     * will not be able to meet this requirement.
     *
     * @return the next step required to finish building the trusonafication.
     */
    FinalizeStep withoutUserPresence();

    /**
     * Sets the time when this authentication request should expire. It cannot be responded to after it expires.
     *
     * @param expiresAt the expiration time.
     * @return the next step required to finish building the trusonafication.
     */
    FinalizeStep expiresAt(Date expiresAt);

    /**
     * Adds a custom field value to the Trusonafication. The custom field will be available in the Trusonafication
     * when it arrives on the mobile device, and can be shown in the UI if using the mobile SDK.
     */
    FinalizeStep customField(String name, Object value);

    /**
     * Returns the trusonafication that was configured by the builder.
     *
     * @return the trusonafication.
     */
    Trusonafication build();
  }

  private abstract static class Builder implements IdentifierStep, ActionStep, ResourceStep, FinalizeStep {

    protected Trusonafication trusonafication;

    private Builder(int desiredLevel) {
      trusonafication = new Trusonafication();
      trusonafication.desiredLevel = desiredLevel;
    }

    @Override
    public ActionStep deviceIdentifier(String deviceIdentifier) {
      trusonafication.deviceIdentifier = deviceIdentifier;
      return this;
    }

    @Override
    public ActionStep truCode(UUID truCodeId) {
      trusonafication.truCodeId = truCodeId;
      return this;
    }

    @Override
    public ActionStep email(String emailAddress) {
      trusonafication.emailAddress = emailAddress;
      return this;
    }

    @Override
    public ActionStep userIdentifier(String userIdentifier) {
      trusonafication.userIdentifier = userIdentifier;
      return this;
    }

    @Override
    public ResourceStep action(String action) {
      trusonafication.action = action;
      return this;
    }

    @Override
    public FinalizeStep resource(String resource) {
      trusonafication.resource = resource;
      return this;
    }

    @Override
    public FinalizeStep expiresAt(Date expiresAt) {
      trusonafication.expiresAt = expiresAt;
      return this;
    }

    @Override
    public FinalizeStep withoutPrompt() {
      trusonafication.prompt = false;
      return this;
    }

    @Override
    public FinalizeStep withoutUserPresence() {
      trusonafication.userPresence = false;
      return this;
    }

    @Override
    public FinalizeStep customField(String name, Object value) {
      if (trusonafication.customFields == null) {
        trusonafication.customFields = new HashMap<>();
      }
      trusonafication.customFields.put(name, value);
      return this;
    }

    @Override
    public Trusonafication build() {
      return trusonafication;
    }
  }

  private static class EssentialBuilder extends Builder {
    private EssentialBuilder() {
      super(2);
    }

    @Override
    public FinalizeStep withoutUserPresence() {
      super.withoutUserPresence();
      trusonafication.desiredLevel = 1;
      return this;
    }
  }

  private static class ExecutiveBuilder extends Builder {
    private ExecutiveBuilder() {
      super(3);
      trusonafication.showIdentityDocument = true;
    }
  }

  /**
   * Creates a builder for creating Essential level {@link Trusonafication} objects.
   *
   * @return an essential level trusonafication builder.
   */
  public static IdentifierStep essential() {
    return new EssentialBuilder();
  }

  /**
   * Creates a builder for creating Executive level {@link Trusonafication} objects.
   *
   * @return an executive level trusonafication builder.
   */
  public static IdentifierStep executive() {
    return new ExecutiveBuilder();
  }


  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(124421, 59938487, this);
  }
}
