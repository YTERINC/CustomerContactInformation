package ru.yterinc.CustomerContactInformation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import ru.yterinc.CustomerContactInformation.models.ContactType;

@Getter
@Setter
@Schema(description = "Сущность контакта")
public class ContactDTO {
    @NotEmpty(message = "Value should not be empty")
    private String contactValue;
    @Enumerated(EnumType.STRING)
    private ContactType type;
}
