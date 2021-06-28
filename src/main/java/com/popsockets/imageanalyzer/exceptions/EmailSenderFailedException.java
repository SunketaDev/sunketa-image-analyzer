package com.popsockets.imageanalyzer.exceptions;

/**
 *
 * @author nandana
 */
public class EmailSenderFailedException  extends CustomException{
    
    public EmailSenderFailedException() {
        super(EMAIL_SENDER_FAILED, "Email sending failed.", "EmailSenderFailed", "Email Sender Failed");
    }
    
    public EmailSenderFailedException(String messease) {
        super(EMAIL_SENDER_FAILED, messease, "EmailSenderFailed", "Email Sender Failed");
    }
}
