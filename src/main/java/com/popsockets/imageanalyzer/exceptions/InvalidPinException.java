package com.popsockets.imageanalyzer.exceptions;

/**
 *
 * @author suranga
 */
public class InvalidPinException extends CustomException {

    public InvalidPinException() {
        super(INVALID_PIN, "Pin validation failed for the request.", "InvalidPin", "Invalid Pin");
    }

    public InvalidPinException(String message) {
        super(INVALID_PIN, message, "InvalidPin", "Invalid Pin");
    }

}
