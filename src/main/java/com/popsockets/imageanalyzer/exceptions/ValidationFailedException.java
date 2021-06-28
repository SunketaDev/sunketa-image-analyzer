package com.popsockets.imageanalyzer.exceptions;

/**
 *
 * @author suranga
 */
public class ValidationFailedException extends CustomException {

    public ValidationFailedException() {
        super(VALIDATION_FAILED, "Validation failed.", "ValidationFailed", "Validation Failed");
    }

    public ValidationFailedException(String message) {
        super(VALIDATION_FAILED, message, "ValidationFailed", "Validation Failed");
    }

}
