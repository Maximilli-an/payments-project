package com.payments.service;

import com.payments.dataModel.Payment;
import com.payments.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    @Autowired
    private PaymentRepository paymentRepository;

    public Payment savePayment(Payment payment) {
        logger.info("Saving payment for: {} {}", payment.getFirstName(), payment.getLastName());

        return paymentRepository.save(payment);
    }
}
