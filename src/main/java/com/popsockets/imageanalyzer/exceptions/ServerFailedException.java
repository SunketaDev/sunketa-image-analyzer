package com.popsockets.imageanalyzer.exceptions;

/**
 *
 * @author nandana
 */
public class ServerFailedException   extends CustomException{
    
    public ServerFailedException() {
        super(SERVER_FAILED, "Exception occured in server.", "ServerFailed", "Internal Server Error");
    }
    
    public ServerFailedException(String message) {
        super(SERVER_FAILED, message, "ServerFailed", "Internal Server Error");
    }
    
}
