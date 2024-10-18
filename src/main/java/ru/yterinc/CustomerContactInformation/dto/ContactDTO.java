package ru.yterinc.CustomerContactInformation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import ru.yterinc.CustomerContactInformation.models.ContactType;

@Schema(description = "Сущность контакта")
public class ContactDTO {
    @NotEmpty(message = "Value should not be empty")
    private String contactValue;

    @Enumerated(EnumType.STRING)
    private ContactType type;

    public @NotEmpty(message = "Value should not be empty") String getContactValue() {
        return contactValue;
    }

    public void setContactValue(@NotEmpty(message = "Value should not be empty") String contactValue) {
        this.contactValue = contactValue;
    }

    public ContactType getType() {
        return type;
    }

    public void setType(ContactType type) {
        this.type = type;
    }
}
