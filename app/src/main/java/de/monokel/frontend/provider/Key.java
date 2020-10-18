package de.monokel.frontend.provider;

/**
 * Class for holding the key and calculating key-values
 *
 * @author Philipp Alessandrini
 * @version 2020-10-17
 */
public class Key {
    private String key;

    public void increase() {
        key = Long.toString(Long.parseLong(key) + 1);
    }

    public String getKey() {
        return key;
    }
}
