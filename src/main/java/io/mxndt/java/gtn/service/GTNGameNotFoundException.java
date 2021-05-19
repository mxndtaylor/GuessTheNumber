package io.mxndt.java.gtn.service;

/**
 * @author mxndt
 */
public class GTNGameNotFoundException extends Exception {

    public GTNGameNotFoundException(String message) {
        super(message);
    }

    public GTNGameNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
