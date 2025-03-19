package com.s8.Crowdfunding.exceptions;

public class InvalidUserAccessException extends RuntimeException {
    public InvalidUserAccessException(String onlyProjectOwnerCanAccess) {
        super(onlyProjectOwnerCanAccess);
    }
}
