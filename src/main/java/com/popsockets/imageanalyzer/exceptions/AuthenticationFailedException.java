package com.popsockets.imageanalyzer.exceptions;

/**
 *
 * @author nandana
 */
public class AuthenticationFailedException extends CustomException{
    
    public AuthenticationFailedException() {
        super(AUTHENTICATION_FAILED, "Request authentication failed.", "AuthenticationFailed", "Authentication Failed");
    }
    
     public AuthenticationFailedException(String message) {
        super(AUTHENTICATION_FAILED, message, "AuthenticationFailed", "Authentication Failed");
    }
}
