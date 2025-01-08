package ru.yterinc.CustomerContactInformation.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import ru.yterinc.CustomerContactInformation.dto.ContactDTO;
import ru.yterinc.CustomerContactInformation.dto.CustomerDTO;
import ru.yterinc.CustomerContactInformation.models.Contact;
import ru.yterinc.CustomerContactInformation.models.ContactType;
import ru.yterinc.CustomerContactInformation.models.Customer;
import ru.yterinc.CustomerContactInformation.services.CustomerService;
import ru.yterinc.CustomerContactInformation.util.CustomerNotFoundException;
import ru.yterinc.CustomerContactInformation.util.CustomerValidator;
import ru.yterinc.CustomerContactInformation.util.ErrorUtility;
import ru.yterinc.CustomerContactInformation.util.NotCreatedException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.OK;

/************************
 Модульное тестирование контроллера
 не актуальная версия
 *************************/

//@ExtendWith(MockitoExtension.class) //Мы используем аннотацию @ExtendWith(MockitoExtension.class),
// чтобы инициализировать моки без явного вызова MockitoAnnotations.openMocks(this).
class CustomerControllerTestV0 {

    @Mock
    private CustomerService customerService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private CustomerValidator customerValidator;

    @InjectMocks
    private CustomerController customerController;

    private Customer customer1;
    private Customer customer2;
    private List<Customer> customers;

    private CustomerDTO customerDTO1;
    private CustomerDTO customerDTO2;
    private List<CustomerDTO> customersDTO;

    private Contact contact1;
    private Contact contact2;
    private List<Contact> contacts;

    private ContactDTO contactDTO1;
    private ContactDTO contactDTO2;
    private List<ContactDTO> contactsDTO;


    @BeforeEach
    void initEach() {
        customer1 = new Customer();
        customer1.setId(1);
        customer1.setName("Test1");

        customer2 = new Customer();
        customer2.setId(2);
        customer2.setName("Test2");
        customers = List.of(customer1, customer2);

        customerDTO1 = new CustomerDTO();
        customerDTO1.setName("Test1");
        customerDTO2 = new CustomerDTO();
        customerDTO2.setName("Test2");
        customersDTO = List.of(customerDTO1, customerDTO2);

        contact1 = new Contact();
        contact1.setId(1);
        contact1.setType(ContactType.EMAIL);
        contact1.setContactValue("test1@mail.ru");
        contact1.setCustomer(customer1);

        contact2 = new Contact();
        contact2.setId(2);
        contact2.setType(ContactType.PHONE);
        contact2.setContactValue("81234567890");
        contact2.setCustomer(customer1);
        contacts = List.of(contact1, contact2);

        contactDTO1 = new ContactDTO();
        contactDTO1.setType(ContactType.EMAIL);
        contactDTO1.setContactValue("test1@mail.ru");

        contactDTO2 = new ContactDTO();
        contactDTO2.setType(ContactType.PHONE);
        contactDTO2.setContactValue("81234567890");
        contactsDTO = List.of(contactDTO1, contactDTO2);

    }

    @Test
    @DisplayName("GET /customer возвращает HTTP-ответ со статусом 200 OK и списком пользователей")
    void getCustomers_ReturnValidResponseEntity() {
        when(customerService.findAll()).thenReturn(customers);
        when(modelMapper.map(customer1, CustomerDTO.class)).thenReturn(customerDTO1);
        when(modelMapper.map(customer2, CustomerDTO.class)).thenReturn(customerDTO2);

        ResponseEntity<List<CustomerDTO>> responseEntity = this.customerController.getCustomers();

        assertNotNull(responseEntity); // Проверяем, что тело ответа не null
        assertEquals(OK, responseEntity.getStatusCode()); // Ожидаем статус 200 OK
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(2, responseEntity.getBody().size());
        assertEquals(customersDTO, responseEntity.getBody());
    }

    @Test
    @DisplayName("GET /customer возвращает HTTP-ответ со статусом 204")
    void getCustomers_ReturnFailedResponseEntity() {
        when(customerService.findAll()).thenReturn(Collections.emptyList());
        ResponseEntity<List<CustomerDTO>> responseEntity = this.customerController.getCustomers();

        assertNotNull(responseEntity); // Проверяем, что тело ответа не null
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("GET /customer/{id} возвращает HTTP-ответ со статусом 200 OK и пользователем")
    void getCustomer_ReturnValidResponseEntity() {
        when(customerService.findOneCustomer(0)).thenReturn(Optional.of(customer1));
        when(modelMapper.map(customer1, CustomerDTO.class)).thenReturn(customerDTO1);

        ResponseEntity<CustomerDTO> responseEntity = this.customerController.getCustomer(0);

        assertNotNull(responseEntity); // Проверяем, что тело ответа не null
        assertEquals(OK, responseEntity.getStatusCode()); // Ожидаем статус 200 OK
        assertEquals(customerDTO1, responseEntity.getBody());
    }

    @Test
    @DisplayName("GET /customer/{id} возвращает исключение")
    void getCustomer_ReturnFailedResponseEntity() {
        when(customerService.findOneCustomer(3)).thenReturn(Optional.empty()); // не существующий id
        assertThrows(CustomerNotFoundException.class, () -> this.customerController.getCustomer(3));  // Проверяем, что выбрасывается исключение
    }

    @Test
    @DisplayName("GET /{id}/contacts возвращает контакты клиента по ID")
    void getContactsById_ReturnValidResponseEntity() {
        when(customerService.findCustomerContacts(1, null)).thenReturn(contacts);
        when(customerService.findCustomerContacts(1, String.valueOf(ContactType.EMAIL))).thenReturn(List.of(contact1));
        when(modelMapper.map(contact1, ContactDTO.class)).thenReturn(contactDTO1);
        when(modelMapper.map(contact2, ContactDTO.class)).thenReturn(contactDTO2);

        ResponseEntity<List<ContactDTO>> responseEntity1 = customerController.getContactsById(1, null);
        ResponseEntity<List<ContactDTO>> responseEntity2 = customerController.getContactsById(1, String.valueOf(ContactType.EMAIL));

        assertNotNull(responseEntity1); // Проверяем, что тело ответа не null
        assertEquals(OK, responseEntity1.getStatusCode()); // Ожидаем статус 200 OK
        assertEquals(2, responseEntity1.getBody().size());
        assertEquals(contactsDTO, responseEntity1.getBody());

        assertNotNull(responseEntity2); // Проверяем, что тело ответа не null
        assertEquals(OK, responseEntity2.getStatusCode()); // Ожидаем статус 200 OK
        assertEquals(1, responseEntity2.getBody().size());
        assertEquals(contactDTO1, responseEntity2.getBody().get(0));
    }

    @Test
    @DisplayName("POST / создаёт нового клиента и возвращает статус 200 OK")
    void addCustomer_ReturnOk() {
        doNothing().when(customerValidator).validate(customerDTO1, bindingResult);
        when(bindingResult.hasErrors()).thenReturn(false); // Указываем, что ошибок нет

//        ResponseEntity<HttpStatus> responseEntity = customerController.addCustomer(customerDTO1, bindingResult);

//        assertEquals(OK, responseEntity.getStatusCode()); // Проверка статуса 200
        verify(customerService).addCustomer(any()); // Проверяем, что метод добавления клиента был вызван
    }

    @Test
    @DisplayName("POST / возвращает исключение при ошибках валидации")
    void addCustomer_ReturnThrows() {
        when(bindingResult.hasErrors()).thenReturn(true); // Указываем, что есть ошибки

        assertThrows(NotCreatedException.class, () -> ErrorUtility.getErrorBindingResult(bindingResult));
        verify(customerService, never()).addCustomer(any()); // Проверяем, что метод добавления клиента не был вызван
    }

    @Test
    @DisplayName("POST /{id}/contact добавляет контакт и возвращает статус 200 OK")
    void addContact_ReturnOk() {
        int userId = 1;
        when(bindingResult.hasErrors()).thenReturn(false); // Указываем, что ошибок нет

        ResponseEntity<HttpStatus> responseEntity = customerController.addContact(userId, contactDTO1, bindingResult);

        assertEquals(OK, responseEntity.getStatusCode()); // Проверка статуса 200
        verify(customerService).addContact(any(), eq(userId)); // Проверяем, что метод добавления контакта был вызван с правильным id
    }

    @Test
    @DisplayName("POST /{id}/contact выбрасывает NotCreatedException при ошибках валидации")
    void addContact_ThrowsNotCreatedException() {
        int userId = 1;
        FieldError fieldError = new FieldError("contactDTO1", "fieldName", "Ошибка валидации");
        when(bindingResult.hasErrors()).thenReturn(true); // Указываем, что есть ошибки
        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(fieldError));

        NotCreatedException exception = assertThrows(NotCreatedException.class, () -> {
            customerController.addContact(userId, contactDTO1, bindingResult);
        });

        assertTrue(exception.getMessage().contains("fieldName - Ошибка валидации")); // Проверьте, что текст ошибки соответствует ожиданиям
        verify(customerService, never()).addContact(any(), anyInt()); // Проверяем, что метод добавления контакта не был вызван
    }

    @Test
    @DisplayName("DELETE /{id} успешно удаляет клиента и возвращает статус 204 No Content")
    void deleteCustomer_ReturnNoContent() {
        int customerId = 1;
        when(customerService.findOneCustomer(customerId)).thenReturn(Optional.of(customer1));

        ResponseEntity<HttpStatus> responseEntity = customerController.deleteCustomer(customerId);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(customerService).deleteCustomer(customerId);
    }

    @Test
    @DisplayName("DELETE /{id} возвращает 404 Not Found, если клиент не существует")
    void deleteCustomer_ReturnNotFound() {
        int customerId = 1;
        when(customerService.findOneCustomer(customerId)).thenReturn(Optional.empty());

        ResponseEntity<HttpStatus> responseEntity = customerController.deleteCustomer(customerId);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        verify(customerService, never()).deleteCustomer(customerId);
    }

    @Test
    @DisplayName("DELETE /{customerId}/contact/{contactId} успешно удаляет контакт и возвращает статус 204 No Content")
    void deleteContact_ReturnNoContent() {
        int customerId = 1;
        int contactId = 1;
        // Настройка мока, чтобы findOneCustomer и findOneContact возвращали существующие объекты
        when(customerService.findOneCustomer(customerId)).thenReturn(Optional.of(customer1));
        when(customerService.findOneContact(contactId)).thenReturn(Optional.of(contact1));

        ResponseEntity<HttpStatus> responseEntity = customerController.deleteContact(customerId, contactId);

        verify(customerService).deleteContact(contactId);
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getBody()); // сравниваем именно с body т.к. мы возвращаем 204 в теле ответа
    }

    @Test
    @DisplayName("DELETE /{customerId}/contact/{contactId} возвращает 404 Not Found, если клиент не существует")
    void deleteContact_ReturnNotFound_WhenCustomerNotFound() {
        int customerId = 5;
        int contactId = 1;
        when(customerService.findOneCustomer(customerId)).thenReturn(Optional.empty());

        ResponseEntity<HttpStatus> responseEntity = customerController.deleteContact(customerId, contactId);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        verify(customerService, never()).deleteContact(contactId);
    }

    @Test
    @DisplayName("DELETE /{customerId}/contact/{contactId} возвращает 404 Not Found, если контакт не существует")
    void deleteContact_ReturnNotFound_WhenContactNotFound() {
        int customerId = 1;
        int contactId = 6;

        when(customerService.findOneCustomer(customerId)).thenReturn(Optional.of(customer1));
        when(customerService.findOneContact(contactId)).thenReturn(Optional.empty());

        ResponseEntity<HttpStatus> responseEntity = customerController.deleteContact(customerId, contactId);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        verify(customerService, never()).deleteContact(contactId);
    }

//    @Test
//    @DisplayName("PUT /{id} успешно обновляет клиента и возвращает статус 200 OK")
//    void updateCustomer_ReturnOk() {
//        int customerId = 1;
//
//        CustomerDTO updatedCustomerDTO = new CustomerDTO();
//        updatedCustomerDTO.setName("newName");
//
//        Customer updatedCustomer = new Customer();
//        updatedCustomer.setName("newName");
//
//        when(customerService.findOneCustomer(customerId)).thenReturn(Optional.of(customer1));
//        doNothing().when(customerValidator).validate(eq(updatedCustomerDTO), eq(bindingResult));
//        when(bindingResult.hasErrors()).thenReturn(false);
//        when(modelMapper.map(updatedCustomerDTO, Customer.class)).thenReturn(updatedCustomer);
//
//        ResponseEntity<HttpStatus> responseEntity = customerController.updateCustomer(customerId, updatedCustomerDTO, bindingResult);
//
//        assertEquals(OK, responseEntity.getStatusCode());
//        verify(customerService).updateCustomer(eq(customerId), any(Customer.class)); // Проверяем, что метод updateCustomer был вызван
//    }

    @Test
    @DisplayName("PUT /{id} выбрасывает исключение, если клиент не существует")
    void updateCustomer_ReturnThrows_WhenCustomerNotFound() {
        int customerId = 1;

        when(customerService.findOneCustomer(customerId)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> customerController.updateCustomer(customerId, new CustomerDTO(), bindingResult));

        verify(customerService, never()).updateCustomer(any(), any());
    }

    @Test
    @DisplayName("PUT /{id} выбрасывает исключение, если данные недействительны")
    void updateCustomer_ReturnThrows_WhenValidationFails() {
        int customerId = 1;

        when(customerService.findOneCustomer(customerId)).thenReturn(Optional.of(customer1));
        when(bindingResult.hasErrors()).thenReturn(true); // Ошибка валидации

        assertThrows(NotCreatedException.class, () -> customerController.updateCustomer(customerId, new CustomerDTO(), bindingResult));
        verify(customerService, never()).updateCustomer(any(), any()); // Проверяем, что данный метод не был вызван
    }

    @Test
    @DisplayName("PUT /{customerId}/contact/{contactId} успешно обновляет контактные данные")
    void updateContact_ReturnOk() {
        int customerId = 1;
        int contactId = 1;
        ContactDTO updateContactDTO = new ContactDTO();
        Contact updatedContact = new Contact();

        when(bindingResult.hasErrors()).thenReturn(false);
        when(customerService.findOneCustomer(customerId)).thenReturn(Optional.of(customer1));
        when(customerService.findOneContact(contactId)).thenReturn(Optional.of(contact1));
        when(modelMapper.map(updateContactDTO, Contact.class)).thenReturn(updatedContact);

        ResponseEntity<HttpStatus> responseEntity = customerController.updateContact(customerId, contactId, updateContactDTO, bindingResult);

        assertEquals(OK, responseEntity.getStatusCode());
        verify(customerService).updateContact(customerId, contactId, updatedContact);
    }

    @Test
    @DisplayName("PUT /{customerId}/contact/{contactId} возвращает 404 Not Found, если клиент не существует")
    void updateContact_ReturnNotFound_WhenCustomerDoesNotExist() {
        int customerId = 1;
        int contactId = 1;

        when(customerService.findOneCustomer(customerId)).thenReturn(Optional.empty());

        ResponseEntity<HttpStatus> responseEntity = customerController.updateContact(customerId, contactId, new ContactDTO(), bindingResult);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        verify(customerService, never()).updateContact(anyInt(), anyInt(), any());
    }

    @Test
    @DisplayName("PUT /{customerId}/contact/{contactId} возвращает 404 Not Found, если контакт не существует")
    void updateContact_ReturnNotFound_WhenContactDoesNotExist() {
        int customerId = 1;
        int contactId = 1;

        when(customerService.findOneCustomer(customerId)).thenReturn(Optional.of(customer1));
        when(customerService.findOneContact(contactId)).thenReturn(Optional.empty());

        ResponseEntity<HttpStatus> responseEntity = customerController.updateContact(customerId, contactId, new ContactDTO(), bindingResult);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        verify(customerService, never()).updateContact(anyInt(), anyInt(), any());
    }

    @Test
    @DisplayName("PUT /{customerId}/contact/{contactId} выбрасывает исключение, если данные недействительны")
    void updateContact_ReturnThrows_WhenValidationFails() {
        int customerId = 1;
        int contactId = 1;

        when(bindingResult.hasErrors()).thenReturn(true);

        assertThrows(NotCreatedException.class, () -> customerController.updateContact(customerId, contactId, new ContactDTO(), bindingResult));
        verify(customerService, never()).updateContact(anyInt(), anyInt(), any());
    }


}

