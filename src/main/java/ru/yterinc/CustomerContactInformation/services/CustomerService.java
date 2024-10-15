package ru.yterinc.CustomerContactInformation.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yterinc.CustomerContactInformation.models.Contact;
import ru.yterinc.CustomerContactInformation.models.Customer;
import ru.yterinc.CustomerContactInformation.models.Email;
import ru.yterinc.CustomerContactInformation.models.Phone;
import ru.yterinc.CustomerContactInformation.repositories.CustomerRepository;
import ru.yterinc.CustomerContactInformation.repositories.EmailRepository;
import ru.yterinc.CustomerContactInformation.repositories.PhoneRepository;
import ru.yterinc.CustomerContactInformation.util.CustomerNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final EmailRepository emailRepository;
    private final PhoneRepository phoneRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, EmailRepository emailRepository, PhoneRepository phoneRepository) {
        this.customerRepository = customerRepository;
        this.emailRepository = emailRepository;
        this.phoneRepository = phoneRepository;
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
    public void addEmail(Integer customerId, Email email) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        email.setCustomer(customer);
        emailRepository.save(email);
    }

    @Transactional
    public void addPhone(Integer customerId, Phone phone) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        phone.setCustomer(customer);
        phoneRepository.save(phone);
    }

    public List<Contact> getContactsByClient(Integer id) {
        List<Contact> contacts = new ArrayList<>();
        contacts.addAll(emailRepository.findByCustomerId(id));


        System.out.println(contacts);
        return contacts;


    }
}


