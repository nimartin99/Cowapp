package de.monokel.frontend.exceptions;

/**
 * Exception related to reporting an infection.
 *
 * @author Philipp Alessandrini
 * @version 2020-10-21
 */
public class KeyNotRequestedException extends Exception {
    /**
     * If an infection is reported before a key is requested.
     * @param message describing exception
     */
    public KeyNotRequestedException(final String message) {
        super(message);
    }
}
