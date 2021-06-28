package com.popsockets.imageanalyzer.exceptions;

/**
 *
 * @author nandana
 */
public class LoginAlreadyInUseException extends CustomException{
    
    public LoginAlreadyInUseException() {
        super(LOGIN_ALREADY_INUSE, "The login is already in use.", "LoginAlreadyInUseException", "Login exists");
    }
    
    public LoginAlreadyInUseException(String messease) {
        super(LOGIN_ALREADY_INUSE, messease, "LoginAlreadyInUseException", "Login exists");
    }
}
