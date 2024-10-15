package ru.yterinc.CustomerContactInformation.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.yterinc.CustomerContactInformation.dto.CustomerDTO;
import ru.yterinc.CustomerContactInformation.dto.EmailDTO;
import ru.yterinc.CustomerContactInformation.dto.PhoneDTO;
import ru.yterinc.CustomerContactInformation.models.Contact;
import ru.yterinc.CustomerContactInformation.models.Customer;
import ru.yterinc.CustomerContactInformation.models.Email;
import ru.yterinc.CustomerContactInformation.models.Phone;
import ru.yterinc.CustomerContactInformation.services.CustomerService;
import ru.yterinc.CustomerContactInformation.services.EmailService;
import ru.yterinc.CustomerContactInformation.util.CustomerErrorResponse;
import ru.yterinc.CustomerContactInformation.util.CustomerNotCreatedException;
import ru.yterinc.CustomerContactInformation.util.CustomerNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final ModelMapper modelMapper;
    private final EmailService emailService;

    @Autowired
    public CustomerController(CustomerService customerService, ModelMapper modelMapper, EmailService emailService) {
        this.customerService = customerService;
        this.modelMapper = modelMapper;
        this.emailService = emailService;
    }

    @GetMapping()
    public List<CustomerDTO> getCustomers() {
        return customerService.findAll().stream().map(this::convertToCustomerDTO).
                collect(Collectors.toList()); // Jackson конвертирует объекты в JSON
    }

    @GetMapping("/{id}")
    public CustomerDTO getCustomer(@PathVariable("id") int id) {
        return convertToCustomerDTO(customerService.findOne(id));
    }

    @GetMapping("/{id}/contacts")
    public ResponseEntity<List<Contact>> getContactsByClient(@PathVariable Integer id) {
        List<Contact> contactList = customerService.getContactsByClient(id);
        return new ResponseEntity<>(contactList, HttpStatus.OK);
//        return null;
    }

    @PostMapping
    public ResponseEntity<HttpStatus> addCustomer(@RequestBody @Valid CustomerDTO customerDTO,
                                                  BindingResult bindingResult) { // вернем сообщение со статусом

        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMsg.append(error.getField())
                        .append(" - ").append(error.getDefaultMessage())
                        .append(";");
            }
            throw new CustomerNotCreatedException(errorMsg.toString());  // выбрасываем исключение,
            // содержащие сообщения об ошибке
        }
        customerService.addCustomer(convertToCustomer(customerDTO));
        //отправляем HTTP ответ с пустым телом и со статусом 200
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/{customerId}/email")
    public ResponseEntity<HttpStatus> addEmail(@PathVariable Integer customerId, @RequestBody EmailDTO emailDTO) {
        customerService.addEmail(customerId, convertToEmail(emailDTO));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/{customerId}/phone")
    public ResponseEntity<HttpStatus> addPhone(@PathVariable Integer customerId, @RequestBody PhoneDTO phoneDTO) {
        customerService.addPhone(customerId, convertToPhone(phoneDTO));
        return ResponseEntity.ok(HttpStatus.OK);
    }


    @ExceptionHandler // здесь ловим исключение, полученное из сервиса если клиент не нашелся в БД
    private ResponseEntity<CustomerErrorResponse> handleException(CustomerNotFoundException e) {
        CustomerErrorResponse response = new CustomerErrorResponse(
                "Customer with id wasn't found",
                System.currentTimeMillis()
        );
        // в HTTP ответе тело ответа (response) и статус в заголовке
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); // NOT_FOUND - 404
    }

    @ExceptionHandler // ловим исключение, если не получилось добавить клиента
    private ResponseEntity<CustomerErrorResponse> handleException(CustomerNotCreatedException e) {
        CustomerErrorResponse response = new CustomerErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        // в HTTP ответе тело ответа (response) и статус в заголовке
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private Customer convertToCustomer(CustomerDTO customerDTO) {
        return modelMapper.map(customerDTO, Customer.class);
    }

    private CustomerDTO convertToCustomerDTO(Customer customer) {
        return modelMapper.map(customer, CustomerDTO.class);
    }

    private Email convertToEmail(EmailDTO emailDTO) {
        return modelMapper.map(emailDTO, Email.class);
    }

    private EmailDTO convertToEmailDTO(Email email) {
        return modelMapper.map(email, EmailDTO.class);
    }

    private Phone convertToPhone(PhoneDTO phoneDTO) {
        return modelMapper.map(phoneDTO, Phone.class);
    }

    private PhoneDTO convertToPhoneDTO(Phone phone) {
        return modelMapper.map(phone, PhoneDTO.class);
    }


}