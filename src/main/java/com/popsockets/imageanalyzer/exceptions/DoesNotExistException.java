package com.popsockets.imageanalyzer.exceptions;

/**
 *
 * @author suranga
 */
public class DoesNotExistException extends CustomException {

    public DoesNotExistException() {
        super(DOES_NOT_EXIST, "Does Not Exist.", "DoesNotExist", "Does Not Exist");
    }

    public DoesNotExistException(String message) {
        super(DOES_NOT_EXIST, message, "DoesNotExist", "Does Not Exist");
    }

}
