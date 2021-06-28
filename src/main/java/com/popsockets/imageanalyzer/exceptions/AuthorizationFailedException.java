package com.popsockets.imageanalyzer.exceptions;

/**
 *
 * @author nandana
 */
public class AuthorizationFailedException extends CustomException{
    
    public AuthorizationFailedException() {
        super(AUTHORIZATION_FAILED, "Request authentication failed.", "AuthorizationFailed", "AuthorizationFailed Failed");
    }
    
     public AuthorizationFailedException(String message) {
        super(AUTHORIZATION_FAILED, message, "AuthorizationFailed", "Authorization Failed");
    }
}
