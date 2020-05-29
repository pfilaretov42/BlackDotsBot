package com.pfilaretov.bdb.exception;

/**
 * main app exception class
 */
public class BlackDotsException extends Exception {

    public BlackDotsException() {
    }

    public BlackDotsException(String message) {
        super(message);
    }

    public BlackDotsException(String message, Throwable cause) {
        super(message, cause);
    }

    public BlackDotsException(Throwable cause) {
        super(cause);
    }
}
