package io.mxndt.java.guessthenumber.service;

/**
 * @author mxndt
 */
public class GuessTheNumberGameNotFoundException extends Exception {

    public GuessTheNumberGameNotFoundException(String message) {
        super(message);
    }

    public GuessTheNumberGameNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
