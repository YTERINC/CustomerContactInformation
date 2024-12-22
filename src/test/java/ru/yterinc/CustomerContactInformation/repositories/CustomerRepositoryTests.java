package ru.yterinc.CustomerContactInformation.repositories;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import ru.yterinc.CustomerContactInformation.models.Customer;
import ru.yterinc.CustomerContactInformation.util.DataUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")  // Используем тестовый профиль
public class CustomerRepositoryTests {
    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    public void setUp() {
        customerRepository.deleteAll();
    }

    @Test
    @DisplayName("Test save customer functionality")
    public void givenCustomerObject_whenSave_thenCustomerIsCreated() {
        //given
        Customer customerToSave = DataUtils.getJohnTransient();
        //when
        Customer savedCustomer = customerRepository.save(customerToSave);
        //then
        assertThat(savedCustomer).isNotNull();
        assertThat(savedCustomer.getId()).isNotNull();
    }

    @Test
    @DisplayName("Test update customer functionality")
    public void givenCustomerToUpdate_whenSave_thenNameIsChanged() {
        //given
        String updatedName = "updatedName";
        Customer customerToCreate = DataUtils.getJohnTransient();
        customerRepository.save(customerToCreate);
        //when
        Customer customerToUpdate = customerRepository.findById(customerToCreate.getId())
                .orElse(null);
        customerToUpdate.setName(updatedName);
        Customer updatedCustomer = customerRepository.save(customerToUpdate);
        //then
        assertThat(updatedCustomer).isNotNull();
        assertThat(updatedCustomer.getName()).isEqualTo(updatedName);
    }

    @Test
    @DisplayName("Test get customer by id functionality")
    public void givenCustomerCreated_whenGetById_thenCustomerIsReturned() {
        //given
        Customer customerToSave = DataUtils.getJohnTransient();
        customerRepository.save(customerToSave);
        //when
        Customer obtainedDeveloper = customerRepository.findById(customerToSave.getId()).orElse(null);
        //then
        assertThat(obtainedDeveloper).isNotNull();
        assertThat(obtainedDeveloper.getName()).isEqualTo("John");
    }

    @Test
    @DisplayName("Test customer not found functionality")
    public void givenCustomerIsNotCreated_whenGetById_thenOptionalIsEmpty() {
        //given
        //when
        Customer obtainedCustomer = customerRepository.findById(1).orElse(null);
        //then
        assertThat(obtainedCustomer).isNull();
    }

    @Test
    @DisplayName("Test get all customers functionality")
    public void givenThreeCustomersAreStored_whenFindAll_thenAllCustomerAreReturned() {
        //given
        Customer developer1 = DataUtils.getJohnTransient();
        Customer developer2 = DataUtils.getFrankTransient();
        Customer developer3 = DataUtils.getMikeTransient();
        customerRepository.saveAll(List.of(developer1, developer2, developer3));
        //when
        List<Customer> obtainedDevelopers = customerRepository.findAll();
        //then
        assertThat(CollectionUtils.isEmpty(obtainedDevelopers)).isFalse();
    }

    @Test
    @DisplayName("Test get customer by name functionality")
    public void givenCustomerSaved_whenGetByName_thenCustomerIsReturned() {
        //given
        Customer customer = DataUtils.getJohnTransient();
        customerRepository.save(customer);
        //when
        Optional<Customer> obtainedCustomer = customerRepository.findByName(customer.getName());
        //then
        assertThat(obtainedCustomer).isNotNull();
        assertThat(obtainedCustomer.get().getName()).isEqualTo(customer.getName());
    }

    @Test
    @DisplayName("Test delete customer by id functionality")
    public void givenCustomerIsSaved_whenDeleteById_thenCustomerIsRemovedFromDB() {
        //given
        Customer developer = DataUtils.getJohnTransient();
        customerRepository.save(developer);
        //when
        customerRepository.deleteById(developer.getId());
        //then
        Customer obtainedDeveloper = customerRepository.findById(developer.getId()).orElse(null);
        assertThat(obtainedDeveloper).isNull();
    }


}
