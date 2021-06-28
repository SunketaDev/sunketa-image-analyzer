package com.popsockets.imageanalyzer.exceptions;

/**
 *
 * @author nandana
 */
public class InvalidAccessTokenException extends CustomException{
    
    public InvalidAccessTokenException() {
        super(INVALID_ACCESS_TOKEN, "Invalid access token.", "InvalidAccessToken", "Invalid Access Token");
    }
    
    public InvalidAccessTokenException(String messease) {
        super(INVALID_ACCESS_TOKEN, messease, "InvalidAccessToken", "Invalid Access Token");
    }
}
