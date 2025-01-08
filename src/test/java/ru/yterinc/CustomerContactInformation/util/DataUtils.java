package ru.yterinc.CustomerContactInformation.util;

import ru.yterinc.CustomerContactInformation.dto.CustomerDTO;
import ru.yterinc.CustomerContactInformation.models.Contact;
import ru.yterinc.CustomerContactInformation.models.ContactType;
import ru.yterinc.CustomerContactInformation.models.Customer;

import java.util.Collections;

public class DataUtils {
    public static Customer getJohnTransient() {
        return Customer.builder()
                .name("John")
                .contactList(Collections.emptyList())
                .build();
    }

    public static Customer getMikeTransient() {
        return Customer.builder()
                .name("Mike")
                .build();
    }

    public static Customer getFrankTransient() {
        return Customer.builder()
                .name("Frank")
                .build();
    }

    public static Customer getJohnPersisted() {
        return Customer.builder()
                .id(1)
                .contactList(Collections.emptyList())
                .name("John")
                .build();
    }

    public static Customer getMikePersisted() {
        return Customer.builder()
                .id(2)
                .name("Mike")
                .build();
    }

    public static Customer getFrankPersisted() {
        return Customer.builder()
                .id(3)
                .name("Frank")
                .build();
    }

    public static Contact getJohnContactTransient() {
        return Contact.builder()
                .contactValue("test123@mail.ru")
                .type(ContactType.EMAIL)
                .build();
    }

    public static Contact getJohnContactPersisted() {
        return Contact.builder()
                .id(1)
                .contactValue("test123@mail.ru")
                .type(ContactType.EMAIL)
                .build();
    }

    public static CustomerDTO getJohnDTOTransient() {
        return CustomerDTO.builder()
                .name("John")
                .build();
    }

    public static CustomerDTO getMikeDTOTransient() {
        return CustomerDTO.builder()
                .name("Mike")
                .build();
    }

    public static CustomerDTO getFrankDTOTransient() {
        return CustomerDTO.builder()
                .name("Frank")
                .build();
    }

    public static CustomerDTO getJohnDTOPersisted() {
        return CustomerDTO.builder()
                .name("John")
                .build();
    }
}
