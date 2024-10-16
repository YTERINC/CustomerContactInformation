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

    public Customer findOne(int id) {
        Optional<Customer> foundCustomer = customerRepository.findById(id);
        return foundCustomer.orElseThrow(CustomerNotFoundException::new); // если клиент не найден, выбрасываем наше исключение
    }

    @Transactional
    public void addCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    @Transactional
    public void addContact(Contact contact, int id) {
        contact.setCustomer(customerRepository
                .findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(String.format("Customer %s not found", id))));
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
        contactRepository.save(contact);
    }


    public List<Contact> findCustomerContacts(int id, String type) {
        if (type == null) {
            return contactRepository.findByCustomerId(id);
        }
        ContactType contactType = ContactType.valueOf(type);
        return contactRepository.findByCustomerIdAndType(id, contactType);
    }

    public Optional<Object> findOneByName(String name) {
        return customerRepository.findByname(name);
    }
}


