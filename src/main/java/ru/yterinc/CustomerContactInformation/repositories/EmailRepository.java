package ru.yterinc.CustomerContactInformation.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yterinc.CustomerContactInformation.models.Email;

import java.util.List;
import java.util.Optional;

public interface EmailRepository extends JpaRepository<Email, Integer> {
    List<Email> findByCustomerId(Integer customerId);

}
