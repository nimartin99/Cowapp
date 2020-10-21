package de.monokel.frontend.provider;

/**
 * Class for holding the unique key for the user.
 *
 * @author Philipp Alessandrini
 * @version 2020-10-21
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
