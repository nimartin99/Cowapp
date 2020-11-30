package de.hhn.frontend.utils;

public class ResponseState {
    public enum State {
        KEY_SUCCESSFULLY_REQUESTED,
        NO_EXISTING_KEY,
        NO_CONNECTION,
        CONTACTS_SUCCESSFULLY_REPORTED,
        NO_DEFINED_CONTACT_TYPE,
        DIRECT_CONTACT,
        INDIRECT_CONTACT,
        NO_CONTACT,
        NO_DEFINED_INFECTION_STATUS,
        NO_USER_KEYS,
        UNDEFINED_REQUEST_BODY_VALUE,
        NO_REGISTERED_CONTACTS
    }

    private static State lastResponseState;

    public static String getLastResponseState() {
        return lastResponseState.name();
    }

    public static void setLastResponseState(State responseState) {
        lastResponseState = responseState;
    }
}
