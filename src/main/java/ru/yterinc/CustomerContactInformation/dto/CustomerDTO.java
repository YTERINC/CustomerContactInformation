package ru.yterinc.CustomerContactInformation.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import ru.yterinc.CustomerContactInformation.models.Email;
import ru.yterinc.CustomerContactInformation.models.Phone;

import java.util.List;

public class CustomerDTO {

    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 30, message = "Name should be between 2 and 30 characters")
    private String name;

    private List<Email> emailList;

    private List<Phone> phoneNumberList;


    public @NotEmpty(message = "Name should not be empty") @Size(min = 2, max = 30, message = "Name should be between 2 and 30 characters") String getName() {
        return name;
    }

    public void setName(@NotEmpty(message = "Name should not be empty") @Size(min = 2, max = 30, message = "Name should be between 2 and 30 characters") String name) {
        this.name = name;
    }

    public List<Email> getEmailList() {
        return emailList;
    }

    public void setEmailList(List<Email> emailList) {
        this.emailList = emailList;
    }

    public List<Phone> getPhoneNumberList() {
        return phoneNumberList;
    }

    public void setPhoneNumberList(List<Phone> phoneNumberList) {
        this.phoneNumberList = phoneNumberList;
    }
}
