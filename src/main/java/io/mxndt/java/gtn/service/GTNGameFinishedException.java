package io.mxndt.java.gtn.service;

/**
 * @author mxndt
 */
public class GTNGameFinishedException extends Exception {

    public GTNGameFinishedException(String message) {
        super(message);
    }

    public GTNGameFinishedException(String message, Throwable cause) {
        super(message, cause);
    }

}
