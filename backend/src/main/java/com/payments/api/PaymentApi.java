package com.payments.api;

import com.payments.dataModel.Payment;
import com.payments.dto.PaymentResponse;
import com.payments.service.PaymentService;
import com.payments.utils.PaymentValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.apache.commons.codec.digest.DigestUtils;

@RestController
@RequestMapping("api/payments")
public class PaymentApi {

    private static final Logger logger = LoggerFactory.getLogger(PaymentApi.class);

    @Autowired
    private PaymentService paymentService;

    @PostMapping
    @Operation(summary = "Create a new payment", description = "Processes a payment and returns a response with a hashed card number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Payment created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<PaymentResponse> savePayment(@RequestBody Payment payment) {

        //        logger.info("Incoming Payment Request {}", payment);

        try {
            //custom validator
            PaymentValidator.validatePayment(payment);

            //Hashing card number earlier before service logic invoked since cardNumber is transient
            payment.setHashedCardNum(DigestUtils.sha256Hex(payment.getCardNumber()));
            payment.setCardNumber(null);

            //Write to DB
            Payment savedPayment = paymentService.savePayment(payment);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(PaymentResponse.success(savedPayment));

        } catch (IllegalArgumentException err) {
            System.err.println("Validation failed: " + err.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(PaymentResponse.error("Failed to save your details: " + err.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(PaymentResponse.error("There has been an error on our side, please note: " + e.getMessage()));
        }
    }
}
