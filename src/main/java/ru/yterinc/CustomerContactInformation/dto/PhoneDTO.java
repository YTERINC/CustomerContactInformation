package ru.yterinc.CustomerContactInformation.dto;

import jakarta.validation.constraints.NotEmpty;

public class PhoneDTO {

    @NotEmpty(message = "Phone number should not be empty")
    private String phoneNumber;

    public @NotEmpty(message = "Phone number should not be empty") String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(@NotEmpty(message = "Phone number should not be empty") String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
