package ru.yterinc.CustomerContactInformation.util;

public class ContactNotValidException extends RuntimeException {
    public ContactNotValidException(String message) {
        super(message);
    }
}