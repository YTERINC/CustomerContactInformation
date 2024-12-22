package ru.yterinc.CustomerContactInformation.service;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yterinc.CustomerContactInformation.models.Contact;
import ru.yterinc.CustomerContactInformation.models.ContactType;
import ru.yterinc.CustomerContactInformation.models.Customer;
import ru.yterinc.CustomerContactInformation.repositories.ContactRepository;
import ru.yterinc.CustomerContactInformation.repositories.CustomerRepository;
import ru.yterinc.CustomerContactInformation.services.CustomerService;
import ru.yterinc.CustomerContactInformation.util.DataUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTests {

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private ContactRepository contactRepository;
    @InjectMocks
    private CustomerService serviceUnderTest;

    @Test
    @DisplayName("Test save customer functionality")
    public void givenCustomerToSave_whenSaveCustomer_thenRepositoryIsCalled() {
        //given
        Customer customerToSave = DataUtils.getJohnTransient();
        BDDMockito.given(customerRepository.save(any(Customer.class)))
                .willReturn(DataUtils.getJohnPersisted());
        //when
        Customer savedCustomer = serviceUnderTest.addCustomer(customerToSave);
        //then
        assertThat(savedCustomer).isNotNull();
    }

    @Test
    @DisplayName("Test update customer functionality")
    public void givenCustomerToUpdate_whenUpdateCustomer_thenRepositoryIsCalled() {
        //given
        Customer customerToUpdate = DataUtils.getJohnPersisted();
        BDDMockito.given(customerRepository.save(any(Customer.class)))
                .willReturn(customerToUpdate);
        //when
        Customer updatedCustomer = serviceUnderTest.updateCustomer(customerToUpdate.getId(), customerToUpdate);
        //then
        assertThat(updatedCustomer).isNotNull();
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    @DisplayName("Test get customer by id functionality")
    public void givenId_whenGetById_thenCustomerIsReturned() {
        //given
        BDDMockito.given(customerRepository.findById(anyInt()))
                .willReturn(Optional.of(DataUtils.getJohnPersisted()));
        //when
        Customer obtainedCustomer = serviceUnderTest.findOneCustomer(1).get();
        //then
        assertThat(obtainedCustomer).isNotNull();
    }

    @Test
    @DisplayName("Test get customer by email functionality")
    public void givenName_whenGetCustomerByName_thenCustomerIsReturned() {
        //given
        String name = "john";
        BDDMockito.given(customerRepository.findByName(anyString()))
                .willReturn(Optional.ofNullable(DataUtils.getJohnPersisted()));
        //when
        Customer obtainedDeveloper = serviceUnderTest.findOneByName(name).get();
        //then
        assertThat(obtainedDeveloper).isNotNull();
    }

    @Test
    @DisplayName("Test get all customers functionality")
    public void givenThreeCustomer_whenGetAll_thenCustomersAreReturned() {
        //given
        Customer customer1 = DataUtils.getJohnPersisted();
        Customer customer2 = DataUtils.getJohnPersisted();
        Customer customer3 = DataUtils.getJohnPersisted();
        BDDMockito.given(customerRepository.findAll()).willReturn(List.of(customer1, customer2, customer3));
        //when
        List<Customer> obtainedCustomers = serviceUnderTest.findAll();
        //then
        assertThat(CollectionUtils.isEmpty(obtainedCustomers)).isFalse();
        assertThat(obtainedCustomers).hasSize(3);
    }

    @Test
    @DisplayName("Test delete by id functionality")
    public void givenCorrectId_whenDeleteById_thenDeleteRepoMethodIsCalled() {
        //given
        //when
        serviceUnderTest.deleteCustomer(1);
        //then
        verify(customerRepository, times(1)).deleteById(anyInt());
    }

    @Test
    @DisplayName("Test save contact functionality")
    public void givenCustomerAndContact_whenSaveContact_thenRepositoryIsCalled() {
        //given
        Customer customer = DataUtils.getJohnPersisted();
        Contact contactToSave = DataUtils.getJohnContactTransient();
        BDDMockito.given(customerRepository.findById(anyInt())).willReturn(Optional.of(customer));
        BDDMockito.given(contactRepository.save(any(Contact.class))).willReturn(DataUtils.getJohnContactPersisted());
        //when
        Contact obtainedContact = serviceUnderTest.addContact(contactToSave, customer.getId());
        //then
        assertThat(obtainedContact).isNotNull();
    }

    @Test
    @DisplayName("Test get all contacts of customer functionality")
    public void givenCustomerWithContacts_whenGetAll_thenContactsAreReturned() {
        //given
        Customer customer = DataUtils.getJohnPersisted();
        Contact contact = DataUtils.getJohnContactPersisted();
        customer.setContactList(List.of(contact));
        BDDMockito.given(contactRepository.findByCustomerId(anyInt())).willReturn(List.of(contact));
        //when
        List<Contact> obtainedContactList = serviceUnderTest.findCustomerContacts(anyInt(),null);
        //then
        assertThat(CollectionUtils.isEmpty(obtainedContactList)).isFalse();
        assertThat(obtainedContactList).hasSize(1);

    }

}
