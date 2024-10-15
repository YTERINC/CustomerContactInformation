package ru.yterinc.CustomerContactInformation.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yterinc.CustomerContactInformation.models.Phone;

public interface PhoneRepository extends JpaRepository<Phone, Integer> {

}
