package ru.yterinc.CustomerContactInformation.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name = "email")
public class Email implements Contact{
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "email_name")
    @jakarta.validation.constraints.Email
    @NotEmpty(message = "Email should not be empty")
    private String emailName;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;

    public Email() {
    }

    public Email(String email, Customer owner) {
        this.emailName = email;
        this.customer = owner;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public @jakarta.validation.constraints.Email @NotEmpty(message = "Email should not be empty") String getEmailName() {
        return emailName;
    }

    public void setEmailName(@jakarta.validation.constraints.Email @NotEmpty(message = "Email should not be empty") String email) {
        this.emailName = email;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer owner) {
        this.customer = owner;
    }
}
