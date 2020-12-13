package de.hhn.cowapp.network;

/**
 * Class for holding the unique key for the user.
 *
 * @author Philipp Alessandrini
 * @version 2020-11-25
 */
public class Key {
    private static String key;

    private static boolean keyRequested;

    public static String getKey() {
        return key;
    }

    public static void setKey(String value) {
        key = value;
    }

    public static void setKeyRequested(boolean status) {
        keyRequested = status;
    }

    public static boolean isKeyRequested() {
        return keyRequested;
    }
}