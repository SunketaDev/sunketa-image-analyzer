package com.popsockets.imageanalyzer.exceptions;

/**
 *
 * @author nandana
 */
public class UserDoesNotFoundException extends CustomException{
    
    public UserDoesNotFoundException() {
        super(USER_DOES_NOT_EXIST, "User cannot be retrieved.", "UserDoesNotFound", "User does not found");
    }
    
    public UserDoesNotFoundException(String messease) {
        super(USER_DOES_NOT_EXIST, messease, "UserDoesNotFound", "User does not found");
    }
}
