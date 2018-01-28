package com.pithadia.marketplace.exception;

import org.springframework.util.StringUtils;

public class UserUnauthorizedException extends Exception {

    public UserUnauthorizedException(Class clazz, String message) {
        super(UserUnauthorizedException.generateMessage(clazz.getSimpleName(), message));
    }

    private static String generateMessage(String entity, String message) {
        return "User " + StringUtils.capitalize(entity) + " is Unauthorized to perform that action.";
    }
}
