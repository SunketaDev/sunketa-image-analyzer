package com.popsockets.imageanalyzer.exceptions;

/**
 *
 * @author nandana
 */
public class AuthServiceFailedException  extends CustomException{
    
    public AuthServiceFailedException() {
        super(AUTH_SERVICE_FAILED, "Operation failed on external auth service.", "AuthServiceFailed", "Auth Service failed");
    }
    
    public AuthServiceFailedException(String message) {
        super(AUTH_SERVICE_FAILED, message, "AuthServiceFailed", "Auth Service failed");
    }
}
