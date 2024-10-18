package ru.yterinc.CustomerContactInformation.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yterinc.CustomerContactInformation.dto.ContactDTO;
import ru.yterinc.CustomerContactInformation.dto.CustomerDTO;
import ru.yterinc.CustomerContactInformation.models.Contact;
import ru.yterinc.CustomerContactInformation.models.Customer;
import ru.yterinc.CustomerContactInformation.services.CustomerService;
import ru.yterinc.CustomerContactInformation.util.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/customer")
@Tag(name = "Контроллер информации клиентах", description = "Управление")
public class CustomerController {

    private final CustomerService customerService;
    private final ModelMapper modelMapper;
    private final CustomerValidator customerValidator;

    @Autowired
    public CustomerController(CustomerService customerService, ModelMapper modelMapper, CustomerValidator customerValidator) {
        this.customerService = customerService;
        this.modelMapper = modelMapper;
        this.customerValidator = customerValidator;
    }

    @GetMapping()
    @Operation(summary = "Получить всех клиентов")
    public List<CustomerDTO> getCustomers() {
        return customerService.findAll().stream().map(this::convertToCustomerDTO).
                collect(Collectors.toList()); // Jackson конвертирует объекты в JSON
    }

    @GetMapping("/{id}")
    @Operation(summary = "Информация клиента по его ID")
    public CustomerDTO getCustomer(@PathVariable("id") int id) {
        return convertToCustomerDTO(customerService.findOne(id));
    }

    @GetMapping("/{id}/contacts")
    @Operation(summary = "Все контакты клиента")
    public List<ContactDTO> getContactsById(@NotNull @PathVariable("id") int id,
                                            @RequestParam(value = "type", required = false) String type) {
        return customerService.findCustomerContacts(id, type)
                .stream()
                .map(n -> modelMapper.map(n, ContactDTO.class))
                .collect(Collectors.toList());
    }


    @PostMapping()
    @Operation(summary = "Создать нового клиента")
    public ResponseEntity<HttpStatus> addCustomer(@RequestBody @Valid CustomerDTO customerDTO,
                                                  BindingResult bindingResult) { // вернем сообщение со статусом
        customerValidator.validate(customerDTO, bindingResult);
        ErrorUtility.getErrorBindingResult(bindingResult);
        customerService.addCustomer(convertToCustomer(customerDTO));
        //отправляем HTTP ответ с пустым телом и со статусом 200
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/{id}/contact")
    @Operation(summary = "Добавление нового контакта пользователю")
    public ResponseEntity<HttpStatus> addContact(@PathVariable("id") int id,
                                                 @RequestBody @Valid ContactDTO contactDTO,
                                                 BindingResult bindingResult) {
        ErrorUtility.getErrorBindingResult(bindingResult);
        customerService.addContact(convertToContact(contactDTO), id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler // здесь ловим исключение, полученное из сервиса если клиент не нашелся в БД
    private ResponseEntity<ErrorResponse> handleException(CustomerNotFoundException e) {
        ErrorResponse response = new ErrorResponse(
                "Customer with id wasn't found",
                System.currentTimeMillis()
        );
        // в HTTP ответе тело ответа (response) и статус в заголовке
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); // NOT_FOUND - 404
    }

    @ExceptionHandler // ловим исключение, если не получилось добавить клиента или контакт
    private ResponseEntity<ErrorResponse> handleException(NotCreatedException e) {
        ErrorResponse response = new ErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        // в HTTP ответе тело ответа (response) и статус в заголовке
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler // ловим исключение, если формат контакта не корректен
    private ResponseEntity<ErrorResponse> handleException(ContactNotValidException e) {
        ErrorResponse response = new ErrorResponse(
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

    private Contact convertToContact(ContactDTO contactDTO) {
        return modelMapper.map(contactDTO, Contact.class);
    }

    private ContactDTO convertToContactDTO(Contact contact) {
        return modelMapper.map(contact, ContactDTO.class);
    }
}