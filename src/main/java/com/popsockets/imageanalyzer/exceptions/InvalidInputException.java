package com.popsockets.imageanalyzer.exceptions;

/**
 *
 * @author nandana
 */
public class InvalidInputException  extends CustomException{
    
    public InvalidInputException() {
        super(INVALID_INPUT, "Input validation failed for the request.", "InvalidInput", "Invalid Input");
    }
    
    public InvalidInputException(String message) {
        super(INVALID_INPUT, message, "InvalidInput", "Invalid Input");
    }
    
}
