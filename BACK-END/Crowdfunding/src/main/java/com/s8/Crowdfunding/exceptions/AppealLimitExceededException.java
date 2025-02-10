package com.s8.Crowdfunding.exceptions;

public class AppealLimitExceededException extends RuntimeException {

    public AppealLimitExceededException(String message) {
        super(message);
    }
}
