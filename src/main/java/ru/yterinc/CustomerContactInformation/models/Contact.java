package ru.yterinc.CustomerContactInformation.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name = "contacts")
public class Contact {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "contact_value")
    @NotEmpty(message = "Value should not be empty")
    private String contactValue;

    @Column(name = "contact_type")
    @Enumerated(EnumType.STRING)
    private ContactType type;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;

    public Contact() {
    }

    public Contact(String contactValue, ContactType type, Customer customer) {
        this.contactValue = contactValue;
        this.type = type;
        this.customer = customer;
    }

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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
