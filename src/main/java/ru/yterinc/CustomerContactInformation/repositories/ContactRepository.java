package ru.yterinc.CustomerContactInformation.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yterinc.CustomerContactInformation.models.Contact;
import ru.yterinc.CustomerContactInformation.models.ContactType;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Integer> {

    List<Contact> findByCustomerId(int id);

    List<Contact> findByCustomerIdAndType(int id, ContactType type);
}
