package ru.yterinc.CustomerContactInformation.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public class EmailDTO {
    @Email
    @NotEmpty(message = "Email should not be empty")
    private String emailName;

    public @Email @NotEmpty(message = "Email should not be empty") String getEmailName() {
        return emailName;
    }

    public void setEmailName(@Email @NotEmpty(message = "Email should not be empty") String emailName) {
        this.emailName = emailName;
    }
}
