package de.hhn.cowapp.network;

/**
 * Class for holding the unique key for the user.
 *
 * @author Philipp Alessandrini
 * @version 2020-12-16
 */
public class Key {
    private static String key;

    public static String getKey() {
        return key;
    }

    public static void setKey(String value) {
        key = value;
    }
}