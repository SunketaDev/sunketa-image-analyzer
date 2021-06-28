package com.popsockets.imageanalyzer.exceptions;

/**
 *
 * @author nandana
 */
public class PersistanceApiFailedException  extends CustomException{
    
    public PersistanceApiFailedException() {
        super(PERSISITANCE_API_FAILED, "Persistance API failed.", "PersistanceApiFailed", "Persistence API failed");
    }
    
    public PersistanceApiFailedException(String message) {
        super(PERSISITANCE_API_FAILED, message, "PersistanceApiFailed", "Persistence API failed");
    }
}
