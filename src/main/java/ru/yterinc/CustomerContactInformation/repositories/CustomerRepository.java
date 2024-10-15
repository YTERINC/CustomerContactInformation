package ru.yterinc.CustomerContactInformation.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yterinc.CustomerContactInformation.models.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

}
