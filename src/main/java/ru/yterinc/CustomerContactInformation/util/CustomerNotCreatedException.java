package ru.yterinc.CustomerContactInformation.util;

public class CustomerNotCreatedException extends RuntimeException {
    public CustomerNotCreatedException(String message) {
        super(message);
    }
}
