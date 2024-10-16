package ru.yterinc.CustomerContactInformation.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;


public class CustomerDTO {

    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 30, message = "Name should be between 2 and 30 characters")
    private String name;

    public @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 30, message = "Name should be between 2 and 30 characters") String getName() {
        return name;
    }

    public void setName(@NotEmpty(message = "Name should not be empty")
                        @Size(min = 2, max = 30, message = "Name should be between 2 and 30 characters") String name) {
        this.name = name;
    }
}
