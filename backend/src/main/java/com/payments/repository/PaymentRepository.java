package com.payments.repository;

import com.payments.dataModel.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    //default CRUD happens because of JPA/Hibernate like save & delete .save(Payment payment) works out of the box
}
