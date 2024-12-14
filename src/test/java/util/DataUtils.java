package util;

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
                .id(11)
                .name("John")
                .build();
    }

    public static Customer getMikePersisted() {
        return Customer.builder()
                .id(12)
                .name("Mike")
                .build();
    }

    public static Customer getFrankPersisted() {
        return Customer.builder()
                .id(13)
                .name("Frank")
                .build();
    }
}
