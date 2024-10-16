package ru.yterinc.CustomerContactInformation.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yterinc.CustomerContactInformation.models.Customer;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    Optional<Object> findByname(String name);
}
