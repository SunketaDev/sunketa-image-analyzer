package com.popsockets.imageanalyzer.exceptions;

/**
 *
 * @author nandana
 */
public class DatabaseException   extends CustomException{
    
    public DatabaseException() {
        super(DATABASE_FAILED, "Database Exception.", "DatabaseException", "Database Exception");
    }
    
    public DatabaseException(String message) {
        super(DATABASE_FAILED, message, "DatabaseException", "Database Exception");
    }
    
}
