package com.popsockets.imageanalyzer.exceptions;

/**
 *
 * @author nandana
 */
public class SnException extends Exception{
    
    final static int ALREADY_EXIST = 501;
    final static int AUTH_SERVICE_FAILED = 502;
    final static int AUTHENTICATION_FAILED = 503;
    final static int EMAIL_SENDER_FAILED = 504;
    final static int INVALID_ACCESS_TOKEN = 505;
    final static int INVALID_INPUT = 506;
    final static int LOGIN_ALREADY_INUSE = 507;
    final static int USER_DOES_NOT_EXIST = 508;
    final static int PERSISITANCE_API_FAILED = 509;
    final static int SERVER_FAILED = 510;
    final static int DATABASE_FAILED = 511;
    final static int DOES_NOT_EXIST = 512;
    final static int UNKNOWN_FAILED = 513;
    
    int errorNo;
    String errorMsg;
    String errorType;
    String error;

    public SnException(int errorNo, String errorMsg, String errorType, String error) {
        super(error + ";" + errorMsg);
        
        this.errorNo = errorNo;
        this.errorMsg = errorMsg;
        this.errorType = errorType;
        this.error = error;
        
    }

    public int getErrorNo() {
        return errorNo;
    }

    public void setErrorNo(int errorNo) {
        this.errorNo = errorNo;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
    
    
    
}
