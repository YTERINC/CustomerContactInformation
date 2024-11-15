package ru.yterinc.CustomerContactInformation.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yterinc.CustomerContactInformation.models.Contact;
import ru.yterinc.CustomerContactInformation.models.ContactType;
import ru.yterinc.CustomerContactInformation.models.Customer;
import ru.yterinc.CustomerContactInformation.repositories.ContactRepository;
import ru.yterinc.CustomerContactInformation.repositories.CustomerRepository;
import ru.yterinc.CustomerContactInformation.util.ContactNotValidException;
import ru.yterinc.CustomerContactInformation.util.CustomerNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@Transactional(readOnly = true)
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final ContactRepository contactRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, ContactRepository contactRepository) {
        this.customerRepository = customerRepository;
        this.contactRepository = contactRepository;
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public Optional<Customer> findOneCustomer(int id) {
        return customerRepository.findById(id);
    }

    public Optional<Contact> findOneContact(int id) {
        return contactRepository.findById(id);
    }

    public List<Contact> findCustomerContacts(int id, String type) {
        if (type == null) {
            return contactRepository.findByCustomerId(id);
        }
        return contactRepository.findByCustomerIdAndType(id, ContactType.valueOf(type));
    }

    public Optional<Customer> findOneByName(String name) {
        return customerRepository.findByName(name);
    }

    @Transactional
    public void addCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    @Transactional
    public void addContact(Contact contact, int id) {
        contact.setCustomer(customerRepository
                .findById(id)
                .orElseThrow(CustomerNotFoundException::new));
        validateContact(contact);
        contactRepository.save(contact);
    }

    @Transactional
    public void deleteCustomer(int id) {
        customerRepository.deleteById(id);
    }

    @Transactional
    public void updateCustomer(Integer id, Customer customer) {
        customer.setId(id);
        customerRepository.save(customer);
    }

    @Transactional
    public void deleteContact(int id) {
        contactRepository.deleteById(id);
    }

    @Transactional
    public void updateContact(int customerId, int contactId, Contact updatedContact) {
        validateContact(updatedContact);
        updatedContact.setId(contactId);
        updatedContact.setCustomer(customerRepository.findById(customerId)
                .orElseThrow(CustomerNotFoundException::new));
        contactRepository.save(updatedContact);
    }

    private void validateContact(Contact contact) {
        switch (contact.getType()) {
            case PHONE -> {
                String regexPattern = "^((\\+7|7|8)+([0-9]){10})$";
                if (!Pattern.compile(regexPattern).matcher(contact.getContactValue()).matches()) {
                    throw new ContactNotValidException("The phone number is incorrect!");
                }
            }
            case EMAIL -> {
                String regexPattern = "^(\\S+)@([a-z0-9-]+)(\\.)([a-z]{2,4})(\\.?)([a-z]{0,4})+$";
                if (!Pattern.compile(regexPattern).matcher(contact.getContactValue()).matches()) {
                    throw new ContactNotValidException("The email is incorrect!");
                }
            }
        }
    }
}


