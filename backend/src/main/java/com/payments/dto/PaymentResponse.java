package com.payments.dto;

import com.payments.dataModel.Payment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    private boolean success;
    private Payment data;
    private String error;

    public static com.payments.dto.PaymentResponse success(Payment payment) {
        return new com.payments.dto.PaymentResponse(true, payment, null);
    }

    public static com.payments.dto.PaymentResponse error(String errorMessage) {
        return new com.payments.dto.PaymentResponse(false, null, errorMessage);
    }

}

