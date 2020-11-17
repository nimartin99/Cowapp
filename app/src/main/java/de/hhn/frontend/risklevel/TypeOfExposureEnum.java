package de.hhn.frontend.risklevel;

/**
 * Enum to classify the type of action with can result in an infection.
 * Direct contact corresponds to a contact with a person that is already reported as infected.
 * Indirect contact corresponds to a contact with a person that is not reported as infected. *
 * Long and short exposure correspond to the duration of the action mentioned above.
 *
 * @author jonas
 * 14.11.2020
 */

public enum TypeOfExposureEnum {LONG_EXPOSURE_DIRECT_CONTACT, SHORT_EXPOSURE_DIRECT_CONTACT, LONG_EXPOSURE_INDIRECT_CONTACT, SHORT_EXPOSURE_INDIRECT_CONTACT, NO_CONTACT, DIRECT_CONTACT, INDIRECT_CONTACT}
