package com.popsockets.imageanalyzer.exceptions;

/**
 *
 * @author nandana
 */
public class TokenExpiredException extends CustomException{
    
    public TokenExpiredException() {
        super(TOKEN_EXPIRED, "Access Token Expired.", "TokenExpired", "Token Expired");
    }
    
     public TokenExpiredException(String message) {
        super(TOKEN_EXPIRED, message, "TokenExpired", "Token Expired");
    }
}
