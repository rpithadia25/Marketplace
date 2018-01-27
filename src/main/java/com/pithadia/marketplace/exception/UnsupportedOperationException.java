package com.pithadia.marketplace.exception;

import org.springframework.util.StringUtils;

public class UnsupportedOperationException extends Exception {

    public UnsupportedOperationException(Class clazz, String message) {
        super(UnsupportedOperationException.generateMessage(clazz.getSimpleName(), message));
    }

    private static String generateMessage(String entity, String message) {
        return "Operation not supported on " + StringUtils.capitalize(entity) + ". " + message;
    }

}
