package com.pithadia.marketplace.exception;

import org.springframework.util.StringUtils;

public class EntityNotFoundException extends Exception {

    public EntityNotFoundException(Class clazz, String searchParamsMap) {
        super(EntityNotFoundException.generateMessage(clazz.getSimpleName(), searchParamsMap));
    }

    private static String generateMessage(String entity, String searchParamsMap) {
        return StringUtils.capitalize(entity) +
                " was not found for { " +
                searchParamsMap + " }";
    }

}
