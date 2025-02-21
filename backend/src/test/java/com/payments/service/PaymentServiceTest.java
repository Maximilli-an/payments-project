package com.payments.service;


import com.payments.dataModel.Payment;
import com.payments.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSavePayment() {
        // Given
        Payment payment = new Payment();
        payment.setId(UUID.randomUUID());
        payment.setFirstName("John");
        payment.setLastName("Doe");
        payment.setZipCode("12345");
        payment.setHashedCardNum("hashed-card-number");

        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        // When
        Payment savedPayment = paymentService.savePayment(payment);

        // Then
        assertNotNull(savedPayment);
        assertEquals("John", savedPayment.getFirstName());
        assertEquals("Doe", savedPayment.getLastName());
        assertEquals("12345", savedPayment.getZipCode());
        assertEquals("hashed-card-number", savedPayment.getHashedCardNum());
    }
}
